# Justificaciones

## Arquitectura cliente-servidor

* Se eligió una arquitectura cliente-servidor porque cubre de forma simple y directa los casos de uso del sistema. Los usuarios necesitan una interfaz gráfica para interactuar con la aplicación, mientras que la lógica de negocio, el procesamiento de datos y la persistencia se centralizan en el servidor.

* Esta separación permite mantener una división clara de responsabilidades: el cliente se encarga de la presentación e interacción con el usuario, y el servidor concentra las reglas del sistema, el acceso a datos y la integración con otros servicios. Además, esta arquitectura facilita el mantenimiento, la seguridad y la evolución futura del sistema.

## Servicios externos de Notificación

* Se decidió utilizar servicios externos de notificacion de mensajes porque cumplen con los requerimientos funcionales del sistema y permiten incorporar canales de comunicación sin desarrollar una solución propia desde cero.

## Broker de mensajeria

* Se decidió incorporar un broker de mensajería para gestionar la comunicación asíncrona entre los gps del camion y nuestro sistema. Esto permite desacoplar a los productores (GPS) y al consumidor de mensajes (Donatrack).

## Division en microservicios

* Se decidió dividir el monolito en los módulos **Donaciones** y **Logística** porque representan responsabilidades claramente distintas dentro del sistema. El módulo de **Donaciones** concentra la gestión de donantes, necesidades, entidades beneficiarias y registro de donaciones, mientras que **Logística** se encarga del seguimiento/auditoria de los camiones, gestion de entregas y creacion de rutas.

* Esta separación permite reducir el acoplamiento, mejorar la mantenibilidad del código y facilitar que cada parte evolucione de forma independiente según las necesidades del negocio. Además, hace más simple asignar responsabilidades al equipo. 

 