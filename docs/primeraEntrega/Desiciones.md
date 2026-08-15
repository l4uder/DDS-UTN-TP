# Justificaciones

## Arquitectura cliente-servidor

* Se eligió una arquitectura cliente-servidor porque cubre de forma simple y directa los casos de uso del sistema. Los usuarios necesitan una interfaz gráfica para interactuar con la aplicación, mientras que la lógica de negocio, el procesamiento de datos y la persistencia se centralizan en el servidor.

* Esta separación permite mantener una división clara de responsabilidades: el cliente se encarga de la presentación e interacción con el usuario, y el servidor concentra las reglas del sistema, el acceso a datos y la integración con otros servicios.

## Servicios externos de mensajería

* Se decidió utilizar servicios externos de mensajería porque cumplen con los requerimientos funcionales del sistema y permiten incorporar canales de comunicación sin desarrollar una solución propia desde cero.
