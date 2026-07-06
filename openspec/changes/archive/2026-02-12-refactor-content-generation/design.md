# Design: Refactorización de Generación de Contenido

## Context

La aplicación actual utiliza OpenAI GPT-4o para procesar transcripciones y generar presentaciones. El servicio `ContentProcessingServiceImpl` hace 3 llamadas secuenciales:
1. Análisis de transcripción
2. Generación de título
3. Generación de image prompts

**Estado Actual:**
- Entidad `Slide`: solo almacena `imagePrompt`, `imageUrl`, `status`, `order`
- Configuración global (`GlobalSettings`) existe pero se usa incorrectamente (concatena "null" en prompts)
- Base de datos: H2 file-based con `spring.jpa.hibernate.ddl-auto=update`
- Sin migración de datos necesaria (proyecto en desarrollo, datos de prueba)

**Stakeholders:**
- Desarrolladores backend (mantenibilidad del código)
- Futuras funcionalidades (edición de slides, regeneración de imágenes)
- Usuarios finales (mejora en la calidad y velocidad de generación)

## Goals / Non-Goals

**Goals:**
- Reducir de 3 llamadas LLM a 2 llamadas más eficientes
- Usar correctamente la configuración global (manejar nulls apropiadamente)
- Almacenar información completa de slides (title, content, imagePrompt)
- Preparar la base para generación de imágenes futura
- Mantener retrocompatibilidad con API existente

**Non-Goals:**
- Implementar generación de imágenes (se hará en cambio separado)
- Agregar edición de slides desde frontend
- Cambiar la API REST de slides (solo agregar campos)
- Migrar datos existentes (no es necesario, proyecto en desarrollo)
- Optimizar llamadas LLM con streaming (puede venir después)

## Decisions

### 1. Nueva Estructura de Llamadas LLM: 2 en vez de 3

**Decisión:** Combinar la generación de título con la generación de slides en una sola llamada que devuelva un JSON completo.

**Rationale:**
- **Eficiencia**: Menos latencia (1 menos round-trip a OpenAI)
- **Costo**: ~33% menos en tokens de entrada (el análisis solo se envía una vez)
- **Coherencia**: El título y las slides se generan juntos, con contexto compartido
- **Mejor estructura**: El LLM puede pensar en el título mientras estructura las slides

**Alternativas consideradas:**
- **Mantener 3 llamadas**: Descartado por ineficiencia
- **Reducir a 1 llamada**: Descartado porque el análisis previo mejora la calidad del resumen

**Implementación:**

```java
// ANTES (3 llamadas):
String analysis = analyzeTranscript(content, settings);
String title = generateTitle(analysis, settings);              // ← ELIMINAR
List<String> prompts = generateImagePrompts(analysis, settings);

// DESPUÉS (2 llamadas):
String analysis = analyzeTranscript(content, settings);
SlideOutline outline = generateSlideOutline(analysis, settings); // outline.title + outline.slides[]
```

### 2. Manejo de Nulls en Configuración: Inserción Condicional en Estructura Base

**Decisión:** Usar una estructura de prompt base predefinida e insertar los campos de configuración condicionalmente en puntos específicos si no son `null`.

**Rationale:**
- **Estructura consistente**: El prompt siempre tiene la misma forma, con secciones opcionales
- **Claridad**: Los puntos de inserción están bien definidos
- **Flexibilidad**: Si el usuario no configura algo, el prompt base sigue siendo efectivo
- **Mantenibilidad**: Fácil de entender qué hace cada configuración

**Estructura del Prompt de Análisis:**

```java
private String buildAnalysisSystemPrompt(GlobalSettings settings) {
    StringBuilder systemPrompt = new StringBuilder();
    
    // Base del prompt (siempre presente)
    systemPrompt.append("Eres un experto en análisis de reuniones y creación de presentaciones. ");
    systemPrompt.append("Tu tarea es analizar transcripts de reuniones y extraer la información ");
    systemPrompt.append("más relevante para crear presentaciones impactantes.\n\n");
    
    // Inserción condicional: systemPrompt del usuario
    if (settings.getSystemPrompt() != null && !settings.getSystemPrompt().isEmpty()) {
        systemPrompt.append("Instrucciones adicionales del usuario: ")
                   .append(settings.getSystemPrompt())
                   .append("\n\n");
    }
    
    // Estructura de análisis (siempre presente)
    systemPrompt.append("Debes analizar el transcript y proporcionar:\n");
    systemPrompt.append("1. Los puntos clave de la reunión\n");
    systemPrompt.append("2. Las decisiones tomadas\n");
    systemPrompt.append("3. Los próximos pasos y responsables\n");
    systemPrompt.append("4. Datos o métricas mencionados\n");
    systemPrompt.append("5. Temas que requieren visualización\n\n");
    systemPrompt.append("Responde en español y de forma estructurada.");
    
    return systemPrompt.toString();
}

private String buildAnalysisUserPrompt(String content, GlobalSettings settings) {
    StringBuilder userPrompt = new StringBuilder();
    
    // Inserción condicional: contentOrientation
    if (settings.getContentOrientation() != null && !settings.getContentOrientation().isEmpty()) {
        userPrompt.append("Enfoque específico para esta presentación: ")
                  .append(settings.getContentOrientation())
                  .append("\n\n");
    }
    
    // Petición base (siempre presente)
    userPrompt.append("Analiza el siguiente transcript de reunión:\n");
    userPrompt.append(content);
    
    return userPrompt.toString();
}
```

**Ejemplo sin configuración:**
```
System: "Eres un experto en análisis de reuniones y creación de presentaciones..."
        (sin sección "Instrucciones adicionales")
User: "Analiza el siguiente transcript de reunión:\n<<contenido>>"
      (sin sección "Enfoque específico")
```

**Ejemplo con configuración:**
```
System: "Eres un experto en análisis de reuniones y creación de presentaciones...
        Instrucciones adicionales del usuario: Eres un experto en análisis de reuniones corporativas
        Debes analizar el transcript..."
User: "Enfoque específico para esta presentación: Orientación a resultados
       Analiza el siguiente transcript de reunión:\n<<contenido>>"
```

**Alternativas consideradas:**
- **Concatenación directa sin estructura**: Descartado por falta de consistencia
- **Templates con placeholders**: Más complejo sin beneficio claro
- **Usar Optional<>**: Overkill para este caso, más verboso

### 3. Esquema JSON de Respuesta: Estructura Completa

**Decisión:** La segunda llamada LLM devuelve un JSON con estructura completa incluyendo título de presentación y toda la info de cada slide.

**Rationale:**
- **Datos ricos**: Almacenamos título, contenido y prompt de imagen por slide
- **Futuras funcionalidades**: Facilita edición, preview, regeneración individual
- **Separación de conceptos**: 
  - `title`: Título corto de la slide
  - `content`: Texto/bullets en español
  - `description`: Prompt EN INGLÉS para generador de imágenes

**Estructura del JSON:**

```json
{
  "title": "Resultados Q4 2025",
  "slides": [
    {
      "slideNumber": 1,
      "title": "Portada",
      "content": "Resultados del Cuarto Trimestre 2025\nAnálisis Ejecutivo",
      "description": "A professional corporate cover slide with dark blue gradient background. Large bold white text reading 'Q4 2025 Results'. Subtitle 'Executive Analysis' in lighter font. Modern minimalist design with company logo in corner. Clean, professional aesthetic."
    },
    {
      "slideNumber": 2,
      "title": "Resumen Financiero",
      "content": "• Ingresos: +25% YoY\n• Margen EBITDA: 32%\n• Cash Flow Positivo",
      "description": "Professional financial summary slide. White background with three key metrics displayed as large numbers with icons. Revenue chart showing upward trend in teal. Clean typography, plenty of white space. Corporate blue accents."
    }
  ]
}
```

**Alternativas consideradas:**
- **Mantener JSON simple (solo imagePrompt)**: Descartado por falta de información
- **Separar content y description en llamadas diferentes**: Descartado por ineficiencia
- **Usar XML en vez de JSON**: Descartado por complejidad de parseo

### 4. Modificación de Entidad Slide: Agregar Campos

**Decisión:** Agregar campos `title` (TEXT) y `content` (TEXT) a la entidad `Slide`. No crear tabla nueva.

**Rationale:**
- **Simplicidad**: H2 con `ddl-auto=update` agrega columnas automáticamente
- **Sin breaking changes**: API actual sigue funcionando, solo añade información
- **Extensibilidad**: Los campos TEXT permiten contenido largo sin límites arbitrarios

**Schema DDL (generado automáticamente):**

```sql
ALTER TABLE slides ADD COLUMN title TEXT;
ALTER TABLE slides ADD COLUMN content TEXT;
```

**Alternativas consideradas:**
- **Crear tabla SlideContent separada (1-to-1)**: Overkill, más joins sin beneficio
- **Usar JSON column**: Descartado por dificultar queries y búsquedas
- **No almacenar title/content**: Descartado por limitar funcionalidades futuras

### 5. Inclusión de visualStyle en Prompt de Generación de Outline

**Decisión:** Incluir `visualStyle` (si no es null) en el system prompt de la segunda llamada LLM, siguiendo la misma estructura base con inserciones condicionales.

**Rationale:**
- **Mejor calidad de descriptions**: El LLM sabe qué estilo visual usar al generar los prompts de imágenes
- **Consistencia**: Todas las slides seguirán el mismo estilo visual configurado
- **Preparación**: Cuando implementemos generación de imágenes, los prompts ya tienen el estilo incorporado
- **Estructura coherente**: Mismo patrón que el prompt de análisis

**Estructura del Prompt de Outline:**

```java
private String buildOutlineGenerationSystemPrompt(GlobalSettings settings) {
    StringBuilder systemPrompt = new StringBuilder();
    
    // Base del prompt (siempre presente)
    systemPrompt.append("Eres un experto diseñador de presentaciones ejecutivas. ");
    systemPrompt.append("Tu tarea es crear un outline detallado para una presentación profesional ");
    systemPrompt.append("basándote en el análisis de una reunión.\n\n");
    
    // Inserción condicional: systemPrompt del usuario
    if (settings.getSystemPrompt() != null && !settings.getSystemPrompt().isEmpty()) {
        systemPrompt.append("Instrucciones adicionales del usuario: ")
                   .append(settings.getSystemPrompt())
                   .append("\n\n");
    }
    
    // Inserción condicional: contentOrientation
    if (settings.getContentOrientation() != null && !settings.getContentOrientation().isEmpty()) {
        systemPrompt.append("Orientación del contenido: ")
                   .append(settings.getContentOrientation())
                   .append("\n\n");
    }
    
    // Inserción condicional: visualStyle
    if (settings.getVisualStyle() != null && !settings.getVisualStyle().isEmpty()) {
        systemPrompt.append("Estilo visual deseado: ")
                   .append(settings.getVisualStyle())
                   .append("\n\n");
    }
    
    // Instrucciones específicas (siempre presentes)
    systemPrompt.append("IMPORTANTE: Debes crear entre 10 y 15 diapositivas para cubrir adecuadamente todo el contenido.\n\n");
    systemPrompt.append("Para cada diapositiva debes proporcionar:\n");
    systemPrompt.append("1. title: Título de la diapositiva (corto, impactante)\n");
    systemPrompt.append("2. content: Contenido principal con los puntos clave en formato de bullets o párrafo\n");
    systemPrompt.append("3. description: Descripción COMPLETA en INGLÉS para generar la imagen de la diapositiva\n\n");
    systemPrompt.append("CRÍTICO para el campo \"description\":\n");
    systemPrompt.append("- Debe ser una descripción en INGLÉS para el modelo de generación de imágenes\n");
    systemPrompt.append("- DEBE incluir el texto EXACTO que debe aparecer visible en la diapositiva\n");
    systemPrompt.append("- Debe describir el layout, colores, tipografía y elementos visuales\n");
    systemPrompt.append("- Incluir instrucciones de diseño específicas\n\n");
    systemPrompt.append("IMPORTANTE: Responde SOLO con un JSON válido sin markdown ni texto adicional.");
    
    return systemPrompt.toString();
}

private String buildOutlineGenerationUserPrompt(String analysis) {
    return String.format(
        "Crea un outline de presentación basándote en este análisis:\n\n%s\n\n" +
        "INSTRUCCIONES:\n" +
        "1. Genera entre 10 y 15 diapositivas según la cantidad y complejidad del contenido\n" +
        "2. La primera diapositiva debe ser una portada con el título principal\n" +
        "3. La última diapositiva debe ser un cierre/conclusiones/próximos pasos\n" +
        "4. Cada \"description\" DEBE incluir el texto exacto que debe aparecer visible en la imagen\n" +
        "5. Las descripciones deben ser en inglés y muy detalladas\n\n" +
        "El formato debe ser:\n" +
        "{\n" +
        "  \"title\": \"Título de la presentación\",\n" +
        "  \"slides\": [\n" +
        "    {\n" +
        "      \"slideNumber\": 1,\n" +
        "      \"title\": \"Título de la diapositiva\",\n" +
        "      \"content\": \"Contenido en español con puntos clave\",\n" +
        "      \"description\": \"Complete English description for image generation...\"\n" +
        "    }\n" +
        "  ]\n" +
        "}",
        analysis
    );
}
```

**Ejemplo sin visualStyle:**
```
System: "Eres un experto diseñador... 
        (sin sección "Estilo visual deseado")
        Para cada diapositiva debes proporcionar..."
```

**Ejemplo con visualStyle:**
```
System: "Eres un experto diseñador...
        Estilo visual deseado: Estilo corporativo moderno, colores azul y blanco...
        Para cada diapositiva debes proporcionar..."
```

**Alternativas consideradas:**
- **Agregar visualStyle solo al user prompt**: Descartado porque el system prompt establece mejor el contexto global
- **No usar visualStyle todavía**: Descartado porque mejora la calidad de las descriptions desde ya

### 6. Parseo JSON: Tolerante a Markdown

**Decisión:** Mantener la limpieza de markdown code blocks (````json`) antes de parsear.

**Rationale:**
- **Resiliencia**: OpenAI a veces envuelve JSON en markdown a pesar de las instrucciones
- **Experiencia previa**: Ya tuvimos este problema y lo solucionamos
- **Bajo costo**: Unas pocas líneas de código previenen errores

**Implementación existente (mantener):**

```java
String cleanedJson = jsonResponse.trim();
if (cleanedJson.startsWith("```")) {
    cleanedJson = cleanedJson.replaceFirst("^```(?:json)?\\s*", "");
    cleanedJson = cleanedJson.replaceFirst("```\\s*$", "");
}
```

## Risks / Trade-offs

### [Risk] JSON más complejo → Mayor chance de parsing errors

**Mitigation:**
- Logging detallado del JSON recibido antes de parsear
- Instrucciones muy claras en el prompt del LLM sobre el formato esperado
- Validar que todos los campos requeridos existan antes de guardar en BD
- Test con ejemplos reales de respuestas LLM

### [Risk] Cambio de schema BD → Downtime o datos inconsistentes

**Mitigation:**
- H2 con `ddl-auto=update` agrega columnas sin downtime
- Nuevos campos permiten NULL, no rompe datos existentes
- En producción futura: usar migraciones con Flyway/Liquibase
- Por ahora: desarrollo, datos de prueba, sin riesgo real

### [Risk] Segunda llamada LLM más larga → Timeout o mayor latencia

**Mitigation:**
- Generar entre 1-12 slides (adaptativo, no forzar 12 siempre)
- Mantener temperature=0.7 para balancear creatividad y velocidad
- Monitorear tiempos de respuesta en logs
- Considerar streaming en futuro cambio si es necesario

### [Risk] Nulls en configuración → Calidad inconsistente de slides

**Mitigation:**
- Documentar bien los defaults (están en el LLM, no en código)
- Mensajes en UI para guiar al usuario a configurar ajustes
- Logs que muestren qué configuración se usó en cada generación
- Trade-off aceptado: flexibilidad > defaults arbitrarios

### [Risk] Descripción (imagePrompt) en inglés → Confusión del usuario

**Mitigation:**
- Campo separado (`content` en español, `description` en inglés)
- Frontend no muestra `description` directamente al usuario por ahora
- Cuando implementemos generación de imágenes, el usuario verá la imagen, no el prompt
- Trade-off: mejor calidad de imágenes > idioma consistente en todos los campos

## Migration Plan

### Despliegue

1. **Preparación:**
   - Backup de `backend/data/` (aunque solo hay datos de prueba)
   - Revisar que `spring.jpa.hibernate.ddl-auto=update` esté activo

2. **Deploy:**
   - Detener backend: `Ctrl+C` o `Stop-Process`
   - Pull/merge cambios
   - `mvn clean package` (recompilar con nuevos cambios)
   - Iniciar backend: `mvn spring-boot:run` o `java -jar target/*.jar`

3. **Verificación:**
   - H2 Console: verificar que columnas `title` y `content` existan en tabla `slides`
   - Crear proyecto de prueba y verificar logs
   - Confirmar que JSON se parsea correctamente
   - Verificar que slides guardadas tienen title y content poblados

### Rollback

**Si el deploy falla:**
1. Detener backend
2. Git revert al commit anterior
3. Recompilar y reiniciar

**Si las columnas ya se agregaron:**
- No hay problema: las columnas nuevas aceptan NULL
- El código anterior simplemente ignorará esos campos
- Para limpieza completa: `Remove-Item -Recurse backend/data/`

### Producción Futura

Cuando se despliegue a producción real (no aplica ahora):
1. Usar herramienta de migraciones (Flyway/Liquibase)
2. Script SQL explícito para agregar columnas
3. Backup de BD antes de migración
4. Despliegue blue-green para zero-downtime

## Open Questions

Ninguna - todos los requisitos están claros y las decisiones técnicas están tomadas. El usuario confirmó:
- Nulls → no agregar al prompt
- Cambiar estructura BD según necesidad
- Refactorizar todo hasta almacenar el prompt de cada diapositiva
- Generación de imágenes queda fuera de scope
