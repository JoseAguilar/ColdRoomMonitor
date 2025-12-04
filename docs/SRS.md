# SRS – Sistema de Monitoreo de Temperaturas en Cuartos Fríos

## 1. Introducción

### 1.1 Propósito del documento

Este documento describe los **requerimientos del sistema de monitoreo de temperaturas en cuartos fríos**, desarrollado como proyecto backend (y posteriormente fullstack) para uso en plantas de alimentos que utilizan refrigeración industrial.

El objetivo del SRS es:

- Definir claramente **qué debe hacer** el sistema.
- Servir como referencia durante el desarrollo y pruebas.
- Permitir que otras personas (desarrolladores, jefes, clientes) entiendan el alcance del proyecto sin ver el código.

### 1.2 Alcance del sistema

El sistema permitirá:

- Registrar automáticamente temperaturas de sensores ubicados en **cuartos fríos** (cámaras de congelación, conservación, túneles, etc.).
- Guardar mediciones históricas en una base de datos.
- Evaluar las mediciones contra reglas de **alarmas** (por temperatura alta/baja).
- Notificar alarmas por **correo electrónico** a los responsables.
- Generar **reportes PDF** con gráficas de temperatura en un rango de fechas.
- Exponer una **API REST** para que un frontend (Angular) consuma la información.

Este SRS describe solo el **backend**. El frontend Angular será definido en otro documento.

### 1.3 Definiciones, acrónimos y abreviaturas

- **Cuarto frío / Cámara**: Espacio refrigerado donde se almacenan productos (congelación, conservación, túnel, etc.).
- **Sensor / SensorDevice**: Dispositivo asociado a un cuarto frío, capaz de medir temperatura (por ejemplo, controlador Danfoss EKC vía Modbus).
- **Medición / Measurement**: Registro de temperatura tomado en un instante específico.
- **DAQ (Data Acquisition)**: Proceso de adquisición de datos. Servicio que lee periódicamente los sensores.
- **Alarm Rule**: Regla que define umbrales (ej. temperatura máxima permitida) y condiciones para disparar una alarma.
- **Alarm Event**: Evento que representa que una condición de alarma se activó (y luego puede resolverse).
- **Modbus**: Protocolo industrial de comunicación utilizado para leer los valores de los controladores.
- **API REST**: Interfaz de programación de aplicaciones basada en HTTP y JSON.
- **Modo Simulación**: Modo de ejecución donde las lecturas de sensores se generan de forma aleatoria/simulada, sin hardware físico.
- **Modo Producción**: Modo de ejecución donde las lecturas de sensores provienen de dispositivos reales vía Modbus.

### 1.4 Stakeholders (interesados)

- **Administrador de planta / Jefe de mantenimiento**: Persona interesada en monitorear temperaturas y recibir alarmas.
- **Operador de cuartos fríos**: Persona que consulta el estado de las cámaras.
- **Desarrollador backend**: Persona que desarrolla y mantiene el sistema.
- **Desarrollador frontend**: Persona que construye la interfaz Angular basada en la API.
- **Auditores / Calidad**: Personal que requiere reportes históricos de temperatura.

---

## 2. Descripción general del sistema

### 2.1 Perspectiva del producto

El sistema es un **backend central** que se comunica con dispositivos industriales (en producción) o con un módulo simulado (en desarrollo) para:

- Leer temperaturas periódicamente.
- Guardarlas en una base de datos relacional.
- Evaluar reglas de alarma.
- Proveer datos y funcionalidades a través de una API REST.

El frontend Angular (no descrito aquí) se conectará a este backend para visualizar:

- Listado de cámaras y sensores.
- Temperaturas actuales.
- Gráficas históricas.
- Alarmas activas e históricas.

### 2.2 Funciones principales del sistema (visión general)

A alto nivel, el sistema realizará:

1. **Adquisición de datos (DAQ)**
    - Leer todos los sensores activos cada minuto.
    - Guardar las mediciones en la base de datos.

2. **Gestión de cuartos fríos y sensores**
    - Crear, consultar, actualizar y deshabilitar cámaras y sensores.

3. **Histórico de mediciones**
    - Permitir consultas de mediciones por sensor y rango de fechas.

4. **Sistema de alarmas**
    - Definir reglas de alarmas por sensor (umbrales, delays).
    - Detectar condiciones de alarma y generar eventos.
    - Marcar alarmas como resueltas cuando la condición desaparece.

5. **Notificaciones por correo**
    - Enviar emails cuando se dispare una alarma (de temperatura alta/baja).

6. **Reportes PDF**
    - Generar reportes descargables con gráficas de temperatura en un rango de fechas.

7. **Autenticación (futuro)**
    - Permitir acceso autenticado a la API (JWT), con roles.

### 2.3 Usuarios (roles previstos)

- **Admin**: Configura cámaras, sensores, reglas de alarma y destinatarios de correo.
- **Viewer / Operador**: Consulta gráficas, estados y reportes.
- **Sistema (DAQ)**: Proceso interno que registra mediciones.

En la primera versión, la autenticación puede ser simple o incluso omitirse, y agregarse después como mejora.

### 2.4 Restricciones

- El registro de datos debe ser **al menos** cada minuto.
- La solución debe basarse en:
    - Backend en **Java 17+** con **Spring Boot**.
    - Base de datos relacional (preferentemente **PostgreSQL**).
- En modo producción, las lecturas se harán vía **Modbus** (TCP o RTU) con los dispositivos especificados.
- En modo simulación, el sistema debe poder ejecutarse **sin hardware físico**, para fines de desarrollo y demostración (GitHub, entrevistas, etc.).

### 2.5 Supuestos

- Existe conectividad entre el servidor backend y los dispositivos Modbus (en producción).
- Los relojes del servidor están correctamente sincronizados.
- Los usuarios tienen acceso al dashboard Angular para consumir la API.
- Se cuenta con un servidor SMTP o servicio externo para envío de correos.

---

## 3. Requerimientos funcionales

### 3.1 Gestión de cuartos fríos

**RF-01** – Crear cuarto frío  
El sistema debe permitir registrar nuevos cuartos fríos con al menos: nombre y estado (habilitado/deshabilitado).

**RF-02** – Listar cuartos fríos  
El sistema debe permitir consultar la lista de cuartos fríos registrados.

**RF-03** – Actualizar cuarto frío  
El sistema debe permitir modificar la información de un cuarto frío.

**RF-04** – Deshabilitar cuarto frío  
El sistema debe permitir deshabilitar un cuarto frío para que no aparezca como activo en las vistas principales (aunque sus datos históricos permanezcan).

---

### 3.2 Gestión de sensores

**RF-05** – Crear sensor  
El sistema debe permitir registrar sensores asociados a un cuarto frío, indicando al menos: nombre, cuarto frío, estado (habilitado/deshabilitado) y, en modo producción, parámetros de Modbus (dirección, registro, etc.).

**RF-06** – Listar sensores  
El sistema debe permitir listar sensores registrados, filtrando por cuarto frío si es necesario.

**RF-07** – Listar sensores activos  
El sistema debe permitir obtener la lista de sensores activos, que serán utilizados por el DAQ.

**RF-08** – Actualizar sensor  
El sistema debe permitir modificar la información de un sensor.

**RF-09** – Deshabilitar sensor  
El sistema debe permitir deshabilitar un sensor para que deje de ser leído por el DAQ.

---

### 3.3 Adquisición de datos (DAQ)

**RF-10** – Adquisición periódica de datos  
El sistema debe ejecutar una tarea programada (scheduler) que, **cada minuto**, recorra todos los sensores activos y obtenga una medición de temperatura para cada uno.

**RF-11** – Lecturas simuladas (modo simulación)  
En modo simulación, el sistema debe generar valores de temperatura de forma aleatoria (dentro de un rango configurable), sin necesidad de hardware físico.

**RF-12** – Lecturas vía Modbus (modo producción)  
En modo producción, el sistema debe obtener la temperatura de los sensores a través de un cliente Modbus, usando la configuración asociada al sensor.

**RF-13** – Registro de mediciones  
Cada lectura de temperatura debe generar una entrada de medición con: sensor, timestamp y valor de temperatura.

---

### 3.4 Histórico de mediciones

**RF-14** – Consultar mediciones por sensor  
El sistema debe permitir obtener las mediciones de un sensor para un rango de fechas y horas indicadas.

**RF-15** – Filtrado por rango de tiempo  
La consulta de mediciones debe aceptar parámetros de fecha/hora de inicio y fin.

---

### 3.5 Reglas y eventos de alarma

**RF-16** – Configurar reglas de alarma  
El sistema debe permitir definir reglas por sensor, especificando al menos:
- Umbral de temperatura alta (highThreshold)
- Umbral de temperatura baja (lowThreshold, opcional)
- Tiempo de retardo (delaySeconds) antes de disparar alarma

**RF-17** – Evaluación de alarma por medición  
Cada vez que se registre una medición, el sistema debe evaluar las reglas de alarma correspondientes al sensor.

**RF-18** – Generación de eventos de alarma  
Si una medición cumple la condición de alarma (por ejemplo, temperatura mayor a highThreshold durante más del delay), el sistema debe crear o actualizar un evento de alarma activo.

**RF-19** – Resolución de alarmas  
Cuando la temperatura vuelva al rango aceptable, el sistema debe marcar el evento de alarma como resuelto.

**RF-20** – Registro histórico de alarmas  
El sistema debe mantener un histórico de eventos de alarma con fecha/hora de disparo y de resolución.

---

### 3.6 Notificaciones por correo

**RF-21** – Configuración de destinatarios  
El sistema debe permitir configurar uno o varios correos de destino para notificaciones de alarmas (ya sea global o por regla).

**RF-22** – Envío de correo en alarma  
Cuando se genere una alarma, el sistema debe enviar un correo electrónico a los destinatarios configurados, indicando: cámara, sensor, tipo de alarma, valor de temperatura y hora.

**RF-23** – Evitar spam de correos  
El sistema debe tener una forma de evitar el envío masivo y repetitivo de correos para la misma alarma (por ejemplo, no reenviar correos mientras una alarma siga activa sin cambios).

---

### 3.7 Reportes PDF

**RF-24** – Generar reporte PDF de temperatura  
El sistema debe permitir generar un reporte PDF con las mediciones de uno o varios sensores en un rango de fechas.

**RF-25** – Incluir gráfica  
El reporte PDF debe incluir al menos una gráfica de temperatura vs tiempo.

**RF-26** – Incluir resumen estadístico  
El reporte PDF debe incluir un resumen con mínimo: temperatura mínima, máxima y promedio del periodo consultado.

---

### 3.8 API REST

**RF-27** – Endpoints para CRUD  
El backend debe exponer endpoints REST para operaciones CRUD de cuartos fríos, sensores, reglas de alarma, etc.

**RF-28** – Endpoint para mediciones  
El backend debe exponer un endpoint para consultar mediciones por sensor y rango de fechas.

**RF-29** – Endpoint para reportes  
El backend debe exponer un endpoint para descargar el reporte PDF generado.

**RF-30** – Documentación de la API  
El backend debe contar con documentación automática de la API (por ejemplo, usando Swagger/OpenAPI), para facilitar el consumo desde el frontend Angular.

---

## 4. Requerimientos no funcionales

### 4.1 Performance

- RNF-01: El sistema debe ser capaz de procesar lecturas de todos los sensores activos cada minuto sin perder datos.
- RNF-02: Las consultas de histórico deben responder en tiempos razonables (por ejemplo, menos de 2 segundos para rangos de hasta 7 días y un número moderado de sensores).

### 4.2 Disponibilidad

- RNF-03: El sistema debe estar diseñado para ejecutarse 24/7 en un entorno de planta.
- RNF-04: En caso de caída temporal de la conexión con los dispositivos Modbus, el sistema debe registrar el error y seguir intentando en el siguiente ciclo de lectura.

### 4.3 Escalabilidad

- RNF-05: La arquitectura debe permitir agregar más sensores y cuartos fríos sin cambios drásticos en el diseño.
- RNF-06: Debe ser posible desplegar el sistema en contenedores (Docker) para escalar horizontalmente si es necesario.

### 4.4 Tecnología y mantenibilidad

- RNF-07: El backend debe desarrollarse con **Java 17+** y **Spring Boot 3+**.
- RNF-08: La persistencia debe implementarse con JPA/Hibernate y una base de datos relacional (PostgreSQL preferida).
- RNF-09: El código debe organizarse en capas (domain, application, infrastructure, api) para facilitar el mantenimiento y la extensibilidad.
- RNF-10: El sistema debe incluir al menos pruebas unitarias básicas para los servicios críticos (por ejemplo, evaluación de reglas de alarma).

### 4.5 Seguridad

- RNF-11: En futuras versiones, el acceso a la API debe requerir autenticación y autorización (JWT, roles).
- RNF-12: No se deben exponer credenciales sensibles en el código fuente (usar configuración externa o variables de entorno).

---

## 5. Fuera de alcance (primera versión)

En la primera versión del sistema NO se incluyen:

- Integración con protocolos distintos a Modbus (ej. MQTT, OPC-UA).
- Control directo de equipos (es un sistema de monitoreo, no de control).
- Redundancia de servidores (High Availability avanzada).
- Gestión compleja de usuarios y permisos (se puede añadir después).
- Panel de configuración 100% gráfico (al inicio la configuración puede hacerse vía API o base de datos).

---

## 6. Futuras extensiones

- Integración con otros sistemas (ERP, WMS).
- Soporte para múltiples plantas o sitios.
- Exportación de datos en formatos adicionales (CSV, Excel).
- Notificaciones por otros canales (SMS, WhatsApp, etc.).
- Dashboards avanzados con KPIs y reportes automáticos programados.
