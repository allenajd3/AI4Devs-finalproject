# Transcripción de reunión técnica — Sistema de Control AML (Aplicativo Web)

**Fecha:** 4 de junio de 2026  
**Duración estimada:** 1 h 45 min  
**Participantes:**
- **Laura Méndez (PO)** — Product Owner, Área de Cumplimiento y Riesgos
- **Carlos Ruiz (PM)** — Project Manager, Dirección de Transformación Digital

**Formato:** Reunión de descubrimiento / definición funcional inicial  
**Objetivo:** Alinear alcance, funcionalidades deseadas, riesgos y cronograma preliminar del nuevo sistema AML web.

---

**[00:00 — Inicio]**

**Carlos:** Buenas tardes, Laura. Gracias por el tiempo. La idea de hoy es salir con un borrador claro de qué queremos construir en el sistema AML, qué no entra en la primera versión, qué riesgos vemos y un timeline realista para presentar a dirección la semana que viene.

**Laura:** Perfecto. Traigo el documento de requerimientos preliminares que armó el equipo de cumplimiento y algunas notas de las auditorías del último trimestre. La presión viene por el hallazgo de la supervisión: necesitamos trazabilidad end-to-end de alertas, decisiones y evidencias antes de diciembre.

**Carlos:** Entendido. Empecemos por el contexto. Hoy ustedes operan con qué: Excel, correo, un core bancario con módulo básico de listas, algo más?

**Laura:** Hoy es un Frankenstein. Las alertas salen del motor del proveedor externo — llamémoslo *Sentinel AML* — y llegan por correo o por un portal que no es muy usable. Los analistas las copian a una planilla compartida, documentan en Word, adjuntan PDFs en carpetas de red y escalan por Teams. No hay un solo lugar donde ver el ciclo de vida de un caso.

**Carlos:** Ok. Entonces el aplicativo web que proponemos sería el **sistema operativo del analista AML**: ingesta de alertas, gestión de casos, workflow, evidencias, reporting y auditoría. ¿El motor de reglas y scoring sigue siendo externo?

**Laura:** Sí, al menos en V1. No queremos reemplazar Sentinel en seis meses; eso sería un riesgo enorme. Lo que sí necesitamos es una capa propia encima: orquestación, caso, decisión, documentación y reportería regulatoria.

**Carlos:** Bien. Voy anotando los bloques funcionales y luego priorizamos MoSCoW.

---

**[00:08 — Visión del producto]**

**Laura:** La visión en una frase: *"Una plataforma web única donde cualquier analista, supervisor o auditor pueda ver en segundos el estado de una alerta, quién la tocó, qué evidencia respalda la decisión y qué quedó reportado al regulador."*

**Carlos:** Me gusta. ¿Usuarios?

**Laura:** Unos 45 analistas de nivel 1 y 2, 8 supervisores, 3 oficiales de cumplimiento, 2 auditores internos con acceso de solo lectura y un administrador funcional. Más adelante, quizá usuarios de negocio para consultas limitadas.

**Carlos:** ¿Multi-sucursal, multi-país?

**Laura:** V1 solo operación local — un país, una entidad legal. Pero la arquitectura tiene que permitir multi-tenant porque en 2027 queremos replicar en la filial de Colombia.

**Carlos:** Anotado: **multi-tenant en diseño, single-tenant en despliegue inicial**.

---

**[00:15 — Funcionalidades deseadas: módulo de alertas]**

**Laura:** Bloque uno: **gestión de alertas**. Necesitamos recibir alertas automáticamente desde Sentinel vía API o, como mínimo, batch nocturno si la API tarda en estar lista. Cada alerta debe normalizarse: ID externo, cliente, producto, monto, regla disparada, score, fecha, canal.

**Carlos:** ¿Duplicados? A veces el mismo evento genera dos alertas.

**Laura:** Sí, regla de deduplicación configurable. Si mismo cliente, misma regla, ventana de 24 horas, consolidar o marcar como relacionada. Eso nos lo pidió auditoría.

**Carlos:** ¿Priorización?

**Laura:** Cola por severidad, SLA y antigüedad. Semáforo visual. Que el supervisor pueda reordenar manualmente en casos excepcionales, pero que quede log.

**Carlos:** ¿Asignación?

**Laura:** Round-robin por equipo en L1, y reasignación manual con motivo obligatorio. Si un analista está de licencia, redistribución masiva.

**Laura:** También quiero **vista 360 del cliente** embebida: datos KYC, productos, transacciones recientes, historial de alertas previas, PEP, listas restrictivas. Hoy abrimos cuatro sistemas.

**Carlos:** Esa vista 360 implica integraciones con core bancario, CRM y posiblemente el data warehouse. ¿Tenemos APIs?

**Laura:** Core sí, documentada pero lenta. CRM parcial. DWH tenemos vistas SQL que BI usa; preferiría no ir directo a producción OLTP.

**Carlos:** Riesgo de integración, lo marco. Para V1, ¿podemos vivir con datos cacheados cada X minutos en lugar de tiempo real?

**Laura:** Para transacciones de los últimos 90 días, refresh cada 15 minutos es aceptable. Para saldos actuales, preferiría near real-time, pero negociable si documentamos la limitación.

---

**[00:28 — Funcionalidades: gestión de casos e investigación]**

**Carlos:** Bloque dos: **casos**.

**Laura:** Una alerta puede convertirse en caso manual o automática según regla — por ejemplo, score alto o monto sobre umbral. Un caso agrupa alertas relacionadas, notas, tareas y decisión final.

**Carlos:** ¿Estados del workflow?

**Laura:** Propongo: *Nuevo → En análisis L1 → Escalado L2 → En revisión supervisor → Pendiente documentación → Cerrado* y ramas: *Escalado a oficial de cumplimiento*, *Reporte ROS*, *Archivado sin acción*. Cada transición con campos obligatorios.

**Carlos:** ¿Configurable por administrador o fijo en código?

**Laura:** En V1 fijo pero parametrizable: listas de motivos de cierre, checklists por tipo de alerta. En V2 workflow designer. No quiero morir en BPMN ahora.

**Carlos:** De acuerdo. **Investigación**: ¿qué herramientas necesitan dentro del caso?

**Laura:** Línea de tiempo de eventos, comparación lado a lado de transacciones sospechosas, notas enriquecidas con @menciones, adjuntos con antivirus y hash, plantillas de dictamen. Y **checklist de debida diligencia reforzada** cuando aplique.

**Carlos:** ¿Colaboración simultánea tipo Google Docs?

**Laura:** No en V1. Bloqueo optimista: si alguien está editando, avisar. Pero comentarios concurrentes sí.

**Carlos:** ¿Búsqueda?

**Laura:** Global: por cliente, ID alerta, RUC/DNI, número de caso, rango de fechas. Filtros guardados. Exportar resultados a CSV auditado.

---

**[00:42 — Listas, screening y KYC]**

**Laura:** Tercer bloque: **listas y screening**. Carga de listas OFAC, ONU, UE, PEP locales, lista interna. Frecuencia diaria y carga ad hoc con validación de formato.

**Carlos:** ¿Matching automático contra clientes y contrapartes?

**Laura:** Sí, pero es sensible. Queremos un job nocturno más **screening on-demand** al crear un caso. Mostrar score de similitud, no solo binario hit/no hit.

**Carlos:** ¿Proveedor de listas o mantenimiento manual?

**Laura:** Híbrido. Proveedor para internacionales, mantenimiento interno para PEP y clientes bloqueados. Trazabilidad: qué versión de lista se usó para cada decisión.

**Laura:** Relacionado: **KYC pendiente y vencido**. Dashboard de clientes con documentación vencida que alimenta riesgo. No es AML puro pero cumplimiento lo quiere en la misma plataforma.

**Carlos:** Eso puede inflar el alcance.

**Laura:** Lo pongo como *Should*. Si no entra, necesitamos al menos un enlace profundo al sistema KYC existente.

---

**[00:52 — Reportes regulatorios y ROS]**

**Carlos:** Bloque cuatro: **reportes**.

**Laura:** Generación asistida de **Reporte de Operación Sospechosa (ROS)**. Plantilla según formato de la UIF, prellenado con datos del caso, editor con validaciones, flujo de aprobación dual: analista + oficial de cumplimiento. Export PDF firmado digitalmente y registro de envío.

**Carlos:** ¿Integración directa con el portal del regulador?

**Laura:** En sueños. En V1 export manual con bitácora: "descargado por X, enviado externamente el día Y". V2 integración si el regulador abre API.

**Laura:** Además: **reportes operativos** — alertas por regla, tiempos de cierre, backlog, productividad por analista, casos reabiertos, tasa de escalamiento. Estos para gerencia, no para el regulador.

**Carlos:** ¿SLA reports?

**Laura:** Sí. Alertas fuera de SLA en rojo, alertas a 80% del SLA en amarillo. Email diario a supervisores configurable.

---

**[01:05 — Seguridad, roles y auditoría]**

**Carlos:** Seguridad. Esto es crítico en AML.

**Laura:** Autenticación con SSO corporativo — Azure AD — MFA obligatorio. RBAC granular: analista L1, L2, supervisor, oficial, auditor, admin. Principio de mínimo privilegio. **Segregación de funciones**: quien cierra no puede ser el único que aprueba un ROS.

**Carlos:** ¿Enmascaramiento de datos sensibles?

**Laura:** En pantalla, enmascarar parcialmente identificadores para roles junior si aplica política. Auditores ven todo read-only.

**Carlos:** **Audit trail** inmutable.

**Laura:** Cada click relevante: login, vista de caso, descarga, cambio de estado, edición de campo, export. Timestamp, usuario, IP, valor anterior/nuevo. Retención mínimo siete años. WORM o equivalente — no borrado, solo anonimización controlada bajo procedimiento legal.

**Carlos:** ¿GDPR / protección de datos personales?

**Laura:** Sí, somos responsables. Minimización, base legal documentada, derecho de acceso gestionado por proceso externo pero el sistema debe poder localizar todos los datos de un titular.

---

**[01:15 — No funcionales y UX]**

**Carlos:** Requisitos no funcionales que tengas en radar.

**Laura:** Web responsive para tablets; el 30% de supervisores revisa en iPad. Tiempo de carga de bandeja principal bajo 3 segundos con 10.000 alertas históricas paginadas. Disponibilidad 99.5% en horario laboral extendido. RPO 1 hora, RTO 4 horas.

**Carlos:** ¿Idioma?

**Laura:** Español. Inglés solo en labels técnicos si hace falta. Accesibilidad WCAG 2.1 AA donde sea viable — hay analistas con baja visión.

**Laura:** UX: menos clics que el portal actual. Bandeja tipo inbox, shortcuts de teclado, dark mode opcional — los analistas trabajan de noche.

**Carlos:** ¿Notificaciones?

**Laura:** In-app + email + opcional Teams webhook para escalamientos y SLA breach.

---

**[01:22 — Priorización MoSCoW]**

**Carlos:** Repasemos MoSCoW para V1.

**Laura:**

**Must have:**
- Ingesta alertas Sentinel (API o batch)
- Bandeja, asignación, SLA
- Casos con workflow estándar
- Vista 360 básica (cliente, alertas previas, transacciones 90d cacheadas)
- Notas, adjuntos, audit trail
- RBAC + SSO
- Reportes operativos básicos
- Export ROS asistido (sin integración regulador)
- Dashboard supervisor

**Should have:**
- Screening listas con versionado
- Deduplicación alertas
- Checklists por tipo
- Notificaciones Teams
- Enlace KYC externo

**Could have:**
- Screening on-demand sub-segundo
- Analítica avanzada
- App móvil nativa

**Won't have (V1):**
- Motor de reglas propio
- Integración directa UIF
- Workflow designer
- Multi-país activo

**Carlos:** Alineados. El *Should* entra si el cronograma apreta; el *Must* es contrato con negocio.

---

**[01:30 — Riesgos]**

**Carlos:** Sección de riesgos. Voy a leer los que veo y completamos.

**Carlos:** **R1 — Integraciones externas.** Dependencia de APIs de Sentinel, core y DWH. Retrasos del proveedor, calidad de datos inconsistente, rate limits.

**Laura:** Probabilidad alta, impacto alto. Mitigación: contrato de interfaz en semana 2, ambiente de integración dedicado, fallback batch, mapeo de errores visible al usuario.

**Carlos:** **R2 — Datos personales sensibles.** Filtración o acceso indebido.

**Laura:** Impacto crítico. Mitigación: cifrado en tránsito y reposo, pentest antes de producción, DLP en descargas, revisión de roles trimestral.

**Carlos:** **R3 — Alcance creep por cumplimiento.** Cada área quiere su reporte custom.

**Laura:** Muy real. Mitigación: comité de cambios quincenal, PO única entrada, congelamiento de alcance post UAT.

**Carlos:** **R4 — Adopción usuarios.** Resistencia a dejar Excel.

**Laura:** Alta. Mitigación: champions en cada turno, capacitación hands-on, periodo de convivencia máximo 4 semanas, métricas de uso ligadas a KPIs de equipo.

**Carlos:** **R5 — Performance con volumen.** Picos de alertas post-regulación nueva.

**Laura:** Sí. Mitigación: pruebas de carga con 3x volumen actual, colas asíncronas, paginación estricta.

**Carlos:** **R6 — Regulatorio / auditoría.** Entregar sin trazabilidad completa.

**Laura:** Impacto crítico. Mitigación: auditoría interna involucrada desde diseño, casos de prueba auditables, UAT con escenarios reales anonimizados.

**Carlos:** **R7 — Dependencia de un proveedor único para listas.**

**Laura:** Medio. Mitigación: contrato SLA, export periódico de listas a nuestro storage.

**Carlos:** **R8 — Timeline agresivo por presión de diciembre.**

**Laura:** El elefante. Mitigación: MVP acotado, go-live por fases — primero alertas y casos, 30 días después ROS y reportes avanzados si hace falta.

**Carlos:** **R9 — Talento técnico.** Especialistas en integración bancaria escasos.

**Laura:** Mitigación: pair con vendor Sentinel, consultor externo 3 meses, documentación obligatoria.

**Carlos:** **R10 — Cambios normativos durante el proyecto.**

**Laura:** Probable. Mitigación: buffer 15% en estimación, arquitectura modular en módulos de reporte.

---

**[01:42 — Arquitectura preliminar (nivel PM)]**

**Carlos:** A alto nivel, propongo: frontend React o similar, backend Java o .NET según estándar IT — ustedes están en Java — API REST, base PostgreSQL, object storage para adjuntos, Redis para colas/cache, despliegue en Kubernetes interno.

**Laura:** Cumplimiento no elige stack; elijo que IT estandarice. Sí quiero **ambientes separados**: dev, QA, preprod espejo prod, prod.

**Carlos:** CI/CD con gates de seguridad. Feature flags para go-live gradual.

**Laura:** ¿SaaS o on-prem?

**Carlos:** On-prem o cloud privada por restricción de datos. No SaaS público.

---

**[01:48 — Estimación y cronograma]**

**Carlos:** Con el Must have, estimación preliminar — equipo de 6: 2 backend, 2 frontend, 1 QA, 1 DevOps half-time, más tú y yo:

| Fase | Duración | Entregable |
|------|----------|------------|
| Discovery detallado + UX | 4 semanas | Wireframes, backlog priorizado, arquitectura |
| Integraciones + fundación | 6 semanas | Ingesta alertas, SSO, modelo de datos |
| Core operativo | 8 semanas | Bandeja, casos, workflow, vista 360 básica |
| Evidencias + auditoría | 4 semanas | Adjuntos, audit trail, reportes básicos |
| ROS + hardening | 4 semanas | Módulo ROS, pruebas carga, pentest |
| UAT + capacitación | 3 semanas | Ajustes, manuales, piloto |
| Go-live + hypercare | 4 semanas | Producción, soporte intensivo |

**Total:** ~33 semanas ≈ **8 meses** desde kickoff.

**Laura:** Hoy estamos en junio. Ocho meses nos deja en febrero 2027. La supervisión pidió evidencia antes de diciembre 2026.

**Carlos:** Entonces escenario **acelerado**: recortar Should, ROS en versión beta manual en fase 2, go-live **fase 1 en noviembre 2026** con Must menos reportes avanzados — 22 semanas, requiere +1 dev backend y aceptar deuda técnica documentada.

**Laura:** Preferiría fase 1 en noviembre con bandeja, casos, audit trail y export ROS básico. Reportes gerenciales avanzados en enero.

**Carlos:** Factible con riesgo medio-alto. Condiciones: decisiones de diseño en 10 días, APIs Sentinel firmadas en 3 semanas, sin cambios Mayores post semana 12.

**Laura:** Lo vendo a dirección así: **MVP noviembre**, **completitud Q1 2027**.

**Carlos:** Hitos de pago / governance: demo quincenal, steering mensual con CTO y Oficial de Cumplimiento.

---

**[01:55 — Criterios de éxito y métricas]**

**Laura:** ¿Cómo medimos éxito?

**Carlos:** Propongo KPIs:

1. **100% alertas** ingresadas al sistema vs correo — meta 30 días post go-live
2. **Tiempo medio de triage** reducido 25% vs baseline Excel
3. **0 alertas críticas fuera de SLA** no visibles en dashboard
4. **100% casos cerrados** con audit trail completo en auditoría muestral
5. **Adopción:** 90% logins diarios analistas activos
6. **Satisfacción** encuesta ≥ 4/5 post hypercare

**Laura:** Agregar: tiempo de generación ROS reducido de 4 horas a 1 hora.

**Carlos:** Anotado.

---

**[01:58 — Próximos pasos]**

**Carlos:** Próximos pasos:
1. Yo formalizo acta y cronograma dual (8 meses / 5.5 meses acelerado) para el viernes.
2. Tú consigues carta de compromiso de APIs con Sentinel y core — owners nombrados.
3. Workshop UX con 4 analistas semana del 16.
4. Sesión de arquitectura con seguridad e infra martes.
5. Borrador de presupuesto: licencias, infra, consultoría listas, 20% contingencia.

**Laura:** Yo traigo tres casos reales anonimizados para user stories y el checklist de auditoría que no podemos fallar.

**Carlos:** ¿Algo más?

**Laura:** Solo remarcar: si no hay **audit trail** creíble, no lanzamos. Prefiero retrasar dos semanas que otro hallazgo.

**Carlos:** De acuerdo. Non-negotiable. Cerramos aquí y te mando la invitación al steering.

**Laura:** Gracias, Carlos. Nos vemos el martes.

**[02:00 — Fin de reunión]**

---

## Anexo A — Resumen ejecutivo (1 página)

| Dimensión | Decisión |
|-----------|----------|
| **Producto** | Plataforma web de gestión AML sobre motor externo (Sentinel) |
| **Usuarios** | ~60 (analistas, supervisores, cumplimiento, auditores) |
| **V1 Must** | Ingesta, bandeja, casos, vista 360 básica, audit trail, RBAC/SSO, ROS asistido |
| **V1 Won't** | Motor reglas propio, integración UIF, multi-país activo |
| **Timeline base** | 8 meses (feb 2027) |
| **Timeline acelerado** | MVP nov 2026 + completitud Q1 2027 |
| **Riesgos top** | Integraciones, plazo diciembre, adopción, datos sensibles |
| **Próximo hito** | Acta + presupuesto viernes; workshop UX semana 16 |

---

## Anexo B — Backlog inicial (extracto user stories)

1. Como analista L1, quiero ver una bandeja priorizada de alertas nuevas para atender primero las de mayor riesgo.
2. Como analista, quiero convertir una alerta en caso y vincular alertas relacionadas.
3. Como supervisor, quiero reasignar casos y ver backlog por SLA.
4. Como analista, quiero consultar transacciones recientes del cliente sin salir del caso.
5. Como oficial de cumplimiento, quiero aprobar un ROS antes de exportar el PDF.
6. Como auditor, quiero consultar quién modificó un campo y cuándo, sin poder editar.
7. Como administrador, quiero gestionar roles y permisos sincronizados con Azure AD.
8. Como analista, quiero adjuntar evidencia con hash y antivirus.
9. Como gerente, quiero un dashboard de productividad y tiempos de cierre.
10. Como sistema, debo registrar cada transición de estado con motivo obligatorio.

---

*Documento generado como material de ejemplo. Personajes y escenarios son ficticios.*
