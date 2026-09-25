# Justificaciones sobre el DER de _DONACIONES_

## Decisión sobre mapeo de relaciones a medioContacto

![Alternativas de Mapeo de relaciones a Contactos](img/alternativasDeMapeoContactos.png)

Para el mapeo de relaciones a MedioContacto (ahora `Contactos`) estábamos entre la **Opción 1** (Tablas intermedias) y la **Opción 2** (Arco Exclusivo con FKs nulas), que se muestran en la imagen.

Analizamos la Opción 1 y nos dimos cuenta de que no necesitamos la flexibilidad de "Muchos a Muchos". En nuestro dominio, un Contacto le pertenece exclusivamente a una entidad, ya que atributos como `esPrincipal` pierden sentido si el contacto se comparte. Si dos personas comparten el mismo teléfono, preferimos que sean dos registros de `MedioContacto` lógicamente distintos.

Por lo tanto, **nos inclinamos fuertemente por la Opción 2**. Entendemos que nos genera un _Arco Exclusivo_ (donde 2 de las 3 FKs siempre serán `NULL`), pero creemos que es un trade-off aceptable para evitar el costo de performance que nos generarían 3 tablas intermedias. 

Además, descartamos la idea de usar una asociación polimórfica genérica (un solo `dueño_id`) para no perder la integridad referencial de las FK en la base de datos. Mientras que, componentes puros de software como `ClienteWhatsapp` o `ClienteCorreo`, se marcaron con `@Transient` ya que no nos interesa persistir los clientes usados; esto corresponde a lógica de negocio.

---