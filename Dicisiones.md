# Justificaciones

Arquitectura cliente-servidor

* Se eligió una arquitectura cliente-servidor porque cubre de forma simple y directa los casos de uso del sistema. Los usuarios necesitan una interfaz gráfica para interactuar con la aplicación, mientras que la lógica de negocio, el procesamiento de datos y la persistencia se centralizan en el servidor.

* Esta separación permite mantener una división clara de responsabilidades: el cliente se encarga de la presentación e interacción con el usuario, y el servidor concentra las reglas del sistema, el acceso a datos y la integración con otros servicios. Además, esta arquitectura facilita el mantenimiento, la seguridad y la evolución futura del sistema.

Servicios externos de mensajería

* Se decidió utilizar servicios externos de mensajería porque cumplen con los requerimientos funcionales del sistema y permiten incorporar canales de comunicación sin desarrollar una solución propia desde cero.

* La mensajería no forma parte del objetivo central del sistema, por lo que delegar esta responsabilidad en servicios especializados reduce la complejidad técnica, mejora la mantenibilidad y permite agregar o reemplazar proveedores de mensajería con menor impacto sobre el resto de la aplicación.

Lenguaje Java

* Se eligió Java como lenguaje de programación porque es una tecnología madura, robusta y ampliamente utilizada en el desarrollo de aplicaciones empresariales. Su soporte para programación orientada a objetos permite modelar de forma clara las entidades, reglas y responsabilidades del dominio del sistema.

* Además, Java cuenta con un ecosistema sólido de herramientas, frameworks y librerías que facilitan el desarrollo, las pruebas, el mantenimiento y la escalabilidad de la aplicación. Esto lo convierte en una opción adecuada para construir un sistema estructurado, mantenible y preparado para evolucionar.