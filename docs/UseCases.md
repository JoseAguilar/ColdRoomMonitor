# Casos de Uso – Sistema de Monitoreo de Temperaturas en Cuartos Fríos

Este documento describe los principales **casos de uso** del sistema, es decir,
las interacciones típicas entre los usuarios (o procesos) y el backend.

---

## UC-01 – Registrar automáticamente mediciones de temperatura (DAQ)

**Actor principal:**
- Proceso interno DAQ (scheduler del backend)

**Objetivo:**  
Registrar mediciones de temperatura de todos los sensores activos cada minuto.

**Precondiciones:**
- Existen sensores registrados y marcados como “activos”.
- El backend está en ejecución.
- El modo de operación (simulación o producción) está correctamente configurado.

**Flujo normal:**
1. Cada 1 minuto, el scheduler DAQ se activa.
2. El sistema obtiene la lista de sensores activos.
3. Para cada sensor:
    1. Si está en modo simulación, genera un valor de temperatura simulado.
    2. Si está en modo producción, lee la temperatura desde el dispositivo vía Modbus.
4. El sistema crea una medición con: sensor, timestamp y valor de temperatura.
5. El sistema guarda la medición en la base de datos.
6. El sistema llama al servicio de alarmas para evaluar la medición.

**Flujos alternos:**
- **FA-01 – Error de lectura en un sensor:**
    - El sistema intenta leer la temperatura y ocurre un error (timeout, dispositivo desconectado, etc.).
    - El sistema registra el error en logs.
    - La medición para ese sensor no se registra en ese ciclo.
    - El DAQ continúa con el siguiente sensor.

**Postcondiciones:**
- Para todos los sensores que se pudieron leer, se ha registrado una medición en la base de datos.
- Las mediciones registradas están disponibles para consultas históricas y evaluación de alarmas.

---

## UC-02 – Configurar un cuarto frío

**Actor principal:**
- Administrador

**Objetivo:**  
Registrar o actualizar la información de un cuarto frío en el sistema.

**Precondiciones:**
- El administrador tiene acceso a la API o interfaz de administración.

**Flujo normal (crear):**
1. El administrador envía una solicitud para crear un nuevo cuarto frío con nombre y estado (habilitado/deshabilitado).
2. El backend valida los datos.
3. El sistema guarda el nuevo cuarto frío en la base de datos.
4. El sistema retorna la información del cuarto frío creado.

**Flujo normal (actualizar):**
1. El administrador consulta un cuarto frío existente.
2. El administrador envía una solicitud para actualizar sus datos (por ejemplo, cambiar el nombre o marcarlo como deshabilitado).
3. El backend valida los datos.
4. El sistema guarda los cambios en la base de datos.
5. El sistema retorna el cuarto frío actualizado.

**Flujos alternos:**
- **FA-01 – Datos inválidos:**
    - El administrador envía datos incompletos o inválidos.
    - El backend responde con un error de validación (400 Bad Request) e indica qué campo está mal.

**Postcondiciones:**
- El cuarto frío queda registrado/actualizado y disponible para asociar sensores.

---

## UC-03 – Registrar un sensor asociado a un cuarto frío

**Actor principal:**
- Administrador

**Objetivo:**  
Registrar un sensor de temperatura y asociarlo a un cuarto frío.

**Precondiciones:**
- Existe al menos un cuarto frío registrado.
- El administrador tiene acceso al sistema.

**Flujo normal:**
1. El administrador selecciona un cuarto frío.
2. El administrador envía una solicitud para crear un sensor, indicando: nombre, cuarto frío asociado, estado (activo/inactivo), y opcionalmente configuración Modbus (en modo producción).
3. El backend valida que el cuarto frío existe.
4. El sistema guarda el sensor en la base de datos.
5. El sistema retorna el sensor creado.

**Flujos alternos:**
- **FA-01 – Cuarto frío inexistente:**
    - El administrador envía un ID de cuarto frío que no existe.
    - El backend responde con un error indicando que el recurso no existe (404 Not Found).

**Postcondiciones:**
- El sensor queda registrado y disponible para ser leído por el DAQ (si está activo).

---

## UC-04 – Consultar histórico de mediciones de un sensor

**Actor principal:**
- Operador / Viewer
- Auditor / Calidad

**Objetivo:**  
Obtener las mediciones de un sensor específico en un intervalo de tiempo.

**Precondiciones:**
- Existen mediciones almacenadas para el sensor.
- El usuario tiene acceso a la API o al frontend que consume la API.

**Flujo normal:**
1. El usuario selecciona un sensor.
2. El usuario selecciona un rango de fechas/horas (desde – hasta).
3. El usuario solicita el histórico al backend.
4. El backend valida los parámetros (sensorId, fechas).
5. El backend consulta las mediciones en la base de datos.
6. El backend retorna una lista de mediciones (timestamp, valor) en formato JSON.
7. El frontend muestra los datos, por ejemplo en una gráfica.

**Flujos alternos:**
- **FA-01 – Sin datos en el rango:**
    - No existen mediciones para el rango indicado.
    - El backend retorna una lista vacía y el frontend puede mostrar un mensaje tipo “No hay datos en el periodo seleccionado”.

**Postcondiciones:**
- El usuario ha obtenido la información histórica necesaria para análisis, auditoría o toma de decisiones.

---

## UC-05 – Configurar reglas de alarma para un sensor

**Actor principal:**
- Administrador

**Objetivo:**  
Definir los umbrales de temperatura y condiciones para disparar una alarma automáticamente.

**Precondiciones:**
- El sensor ya está registrado.
- El administrador tiene acceso a la API o interfaz de configuración.

**Flujo normal:**
1. El administrador selecciona un sensor.
2. El administrador indica los valores de:
    - Temperatura alta (highThreshold).
    - Temperatura baja (lowThreshold), si aplica.
    - Tiempo de retardo (delaySeconds).
3. El administrador envía la solicitud al backend.
4. El backend valida los valores (por ejemplo, que sean numéricos y tengan sentido).
5. El sistema crea o actualiza la regla de alarma en la base de datos.
6. El sistema retorna la regla de alarma guardada.

**Flujos alternos:**
- **FA-01 – Datos de regla inválidos:**
    - Se envían valores fuera de rango o inconsistente (por ejemplo, lowThreshold > highThreshold).
    - El backend responde con un error de validación.

**Postcondiciones:**
- La regla de alarma queda registrada y será utilizada automáticamente al evaluar nuevas mediciones.

---

## UC-06 – Disparar una alarma de temperatura alta

**Actor principal:**
- Proceso interno de alarma (AlarmService)

**Objetivo:**  
Detectar que un sensor ha superado el umbral de temperatura alta durante el tiempo de retardo configurado y generar una alarma.

**Precondiciones:**
- Existe una regla de alarma configurada para el sensor (con highThreshold y delaySeconds).
- El DAQ está en funcionamiento y registrando mediciones.

**Flujo normal:**
1. Se registra una nueva medición para un sensor.
2. El sistema evalúa esa medición usando la regla de alarma.
3. El sistema detecta que la temperatura está por encima del highThreshold.
4. El sistema verifica si se ha mantenido por encima del umbral durante al menos delaySeconds (puede requerir comparar varias mediciones).
5. Si se cumple la condición de tiempo, el sistema:
    - Crea un nuevo evento de alarma activo, o
    - Actualiza un evento existente (si ya estaba en estado de alarma).
6. El sistema solicita al módulo de notificaciones el envío de correo (ver UC-07).

**Flujos alternos:**
- **FA-01 – Temperatura vuelve al rango normal antes de cumplir el delay:**
    - La temperatura supera el umbral, pero luego regresa a la normalidad antes de completarse delaySeconds.
    - El sistema NO genera la alarma.

**Postcondiciones:**
- Existe un evento de alarma activo registrado en la base de datos para ese sensor.

---

## UC-07 – Enviar correo de notificación de alarma

**Actor principal:**
- Sistema (servicio de notificaciones)

**Objetivo:**  
Notificar por correo electrónico a los destinatarios configurados cuando se dispara una alarma.

**Precondiciones:**
- Se ha generado un evento de alarma activo (ver UC-06).
- Están configurados uno o varios correos de destinatarios.
- La configuración SMTP está correctamente definida.

**Flujo normal:**
1. El servicio de alarmas determina que debe enviarse una notificación por correo.
2. El sistema construye el contenido del correo, incluyendo:
    - Cámara / cuarto frío.
    - Sensor.
    - Tipo de alarma (por ejemplo, temperatura alta).
    - Valor de temperatura.
    - Fecha y hora del evento.
3. El sistema envía el correo a los destinatarios configurados.
4. El sistema puede registrar internamente que ya se notificó esa alarma (para evitar envíos repetidos).

**Flujos alternos:**
- **FA-01 – Error al enviar correo:**
    - El servidor SMTP no responde o las credenciales son incorrectas.
    - El sistema registra un error en logs.
    - La alarma se mantiene activa, pero sin notificación por correo.
- **FA-02 – Evitar spam por misma alarma:**
    - Si la alarma sigue activa y no hay cambios, el sistema no debe reenviar correos continuamente (esto puede controlarse con reglas adicionales en el código).

**Postcondiciones:**
- Los destinatarios han sido informados de la alarma (salvo errores de SMTP).
- El evento de alarma continúa activo hasta que se resuelva (ver UC-08).

---

## UC-08 – Resolver una alarma cuando la temperatura vuelve a la normalidad

**Actor principal:**
- Sistema de alarmas

**Objetivo:**  
Marcar una alarma como resuelta cuando las mediciones vuelvan a estar dentro del rango normal.

**Precondiciones:**
- Existe un evento de alarma activo para un sensor.
- Se siguen registrando mediciones para ese sensor.

**Flujo normal:**
1. El DAQ registra una nueva medición para el sensor en alarma.
2. El sistema evalúa la medición y detecta que la temperatura ha regresado al rango aceptable (por debajo del highThreshold y/o por encima del lowThreshold, según regla).
3. El sistema marca el evento de alarma como resuelto (active = false) y registra la hora de resolución.
4. Opcionalmente, el sistema puede enviar un correo indicando que la condición de alarma terminó.

**Flujos alternos:**
- **FA-01 – Mediciones siguen fuera de rango:**
    - La temperatura continúa fuera de rango.
    - El evento de alarma permanece activo.

**Postcondiciones:**
- El evento de alarma queda cerrado y se conserva como parte del histórico de alarmas.

---

## UC-09 – Generar un reporte PDF de temperaturas

**Actor principal:**
- Operador / Auditor / Admin

**Objetivo:**  
Obtener un archivo PDF con la gráfica y resumen de temperaturas en un periodo.

**Precondiciones:**
- Existen mediciones históricas en el rango de fechas solicitado.
- El servicio de generación de reportes está implementado.

**Flujo normal:**
1. El usuario selecciona:
    - Uno o varios sensores (o una cámara específica).
    - Rango de fechas y horas.
2. El usuario solicita generar el reporte al backend (por medio de la API).
3. El backend valida los parámetros.
4. El backend obtiene las mediciones correspondientes desde la base de datos.
5. El backend genera una gráfica de temperatura vs tiempo.
6. El backend crea un documento PDF que incluye:
    - Información básica (cámara, sensor, rango de fechas).
    - Gráfica.
    - Estadísticas (mínimo, máximo, promedio).
7. El backend devuelve el archivo PDF como respuesta.
8. El frontend ofrece la descarga al usuario.

**Flujos alternos:**
- **FA-01 – No hay datos en el rango:**
    - El backend detecta que no hay mediciones para ese rango.
    - O bien genera un reporte indicando “sin datos”, o responde con un mensaje de error controlado.

**Postcondiciones:**
- El usuario cuenta con un PDF descargado que puede usar para auditorías o documentación.

---

## UC-10 – Consultar cámaras, sensores, alarmas y estado general

**Actor principal:**
- Operador / Viewer

**Objetivo:**  
Ver el estado actual del sistema (cámaras, sensores, temperaturas recientes y alarmas activas).

**Precondiciones:**
- El backend está en ejecución.
- Existen cámaras, sensores y mediciones registradas.

**Flujo normal:**
1. El usuario accede al dashboard (frontend Angular).
2. El frontend consulta la API para:
    - Listar cámaras (cuartos fríos).
    - Listar sensores por cámara.
    - Obtener últimas mediciones por sensor (por ejemplo, últimas N mediciones o último valor).
    - Obtener alarmas activas.
3. El backend responde con la información solicitada.
4. El frontend muestra:
    - Lista de cámaras.
    - Lista de sensores y su última temperatura.
    - Indicadores visuales de alarmas activas.

**Flujos alternos:**
- **FA-01 – Sin mediciones recientes:**
    - Si no hay mediciones recientes, el frontend puede mostrar un estado “Sin datos” o “No disponible” para ciertos sensores.

**Postcondiciones:**
- El usuario tiene una visión clara del estado actual de los cuartos fríos y sensores.
