# Configuración de VS Code / Cursor

Este directorio contiene las configuraciones para ejecutar y depurar fácilmente el proyecto desde VS Code o Cursor.

## 🚀 Inicio Rápido

### Opción 1: Arrancar Todo (Recomendado)

1. Abre la paleta de comandos: `Ctrl+Shift+P` (Windows) o `Cmd+Shift+P` (Mac)
2. Escribe "Tasks: Run Task"
3. Selecciona **"Start All Services"**

Esto arrancará automáticamente:
- Backend (Spring Boot) en `http://localhost:8080`
- Frontend (Vite) en `http://localhost:5174`

### Opción 2: Arrancar Servicios por Separado

#### Backend
1. `Ctrl+Shift+P` → "Tasks: Run Task"
2. Selecciona **"Backend: Spring Boot Run"**

#### Frontend
1. `Ctrl+Shift+P` → "Tasks: Run Task"
2. Selecciona **"Frontend: Vite Dev Server"**

## 🐛 Debug

### Debug del Backend (Java)

1. Ve a la vista de Debug (`Ctrl+Shift+D`)
2. Selecciona una de estas configuraciones:
   - **"Debug Backend (Spring Boot)"** - Debug básico
   - **"Debug Backend with OpenAI"** - Debug con API Key de OpenAI (te pedirá la key)
3. Presiona `F5` para iniciar

### Debug del Frontend (TypeScript)

El frontend se ejecuta con Vite que incluye Hot Module Replacement (HMR), por lo que los cambios se reflejan automáticamente. Para debug en el navegador:

1. Abre Chrome DevTools (`F12`)
2. Usa la pestaña "Sources" para establecer breakpoints en el código TypeScript

## ⚙️ Configuración

### Java Home

El proyecto está configurado para usar Java 24 en:
```
C:\JAVA\openjdk-24
```

Si tu instalación de Java está en otra ubicación, actualiza:
- `.vscode/tasks.json` → `JAVA_HOME`
- `.vscode/launch.json` → `env.JAVA_HOME`
- `.vscode/settings.json` → `maven.terminal.customEnv`

### OpenAI API Key

Para que el backend funcione correctamente, necesitas configurar tu API Key de OpenAI:

**Opción 1: Variable de entorno (Recomendado)**
```powershell
$env:OPENAI_API_KEY = "tu-api-key-aquí"
```

**Opción 2: Al hacer debug**
Usa la configuración "Debug Backend with OpenAI" que te pedirá la key al iniciar.

**Opción 3: application.properties**
Edita `backend/src/main/resources/application.properties`:
```properties
spring.ai.openai.api-key=tu-api-key-aquí
```

⚠️ **NUNCA** hagas commit de tu API key real al repositorio.

## 📁 Archivos de Configuración

- **`launch.json`** - Configuraciones de debug y compound launches
- **`tasks.json`** - Tareas para build y run
- **`settings.json`** - Configuración del workspace (Java, Maven, TypeScript)

## 🔧 Atajos de Teclado Útiles

- `Ctrl+Shift+B` - Ejecutar tarea de build por defecto (Start All Services)
- `F5` - Iniciar debug
- `Ctrl+F5` - Ejecutar sin debug
- `Shift+F5` - Detener debug
- `Ctrl+Shift+D` - Abrir vista de debug

## 💡 Tips

1. **Terminal Integrada**: Los servicios se ejecutan en paneles dedicados del terminal integrado
2. **Auto Reload**: Tanto el backend (Spring Boot DevTools) como el frontend (Vite HMR) tienen recarga automática
3. **Logs**: Revisa los logs en los paneles de terminal para ver el progreso de cada servicio
4. **Puerto en Uso**: Si ves errores de puerto ocupado, usa `netstat -ano | findstr :8080` para encontrar y matar el proceso

## 🐞 Troubleshooting

### Backend no arranca
- Verifica que Java 24 esté instalado en `C:\JAVA\openjdk-24`
- Asegúrate de que el puerto 8080 no esté en uso
- Revisa que Maven esté correctamente configurado

### Frontend no arranca
- Ejecuta `npm install` en la carpeta `frontend/`
- Verifica que Node.js esté instalado
- Si el puerto 5173 está ocupado, Vite intentará usar otro automáticamente

### OpenAI API Errors
- Verifica que tu API key sea válida
- Asegúrate de tener créditos en tu cuenta de OpenAI
- Revisa los logs del backend para más detalles
