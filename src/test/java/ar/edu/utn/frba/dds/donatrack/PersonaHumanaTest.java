package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ProveedorClienteCorreo;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PersonaHumanaTest {

  @BeforeEach
  void setUp() {
    //Mock de Motor de correos exclusivo para los tests
    ProveedorClienteCorreo.inicializar((destino, mensaje) -> {
        // No hace nada de red, solo simula que lo envió
        System.out.println("TEST - Simulando envío a: " + destino);
    });
  }

  @Test
  public void sePuedeCrearUnaPersonaHumanaConDatosValidos() {
    PersonaHumana persona = new PersonaHumanaBuilder().conNombre("Esteban")
        .conDocumento(new Documento(TipoDocumento.DNI, "45123456"))
        .conContactoPrincipal(new CorreoDeContato("estebancarp@gmail.com")).build();

    assertNotNull(persona.getContactoPrincipal());
    assertEquals("Esteban", persona.getNombre());
  }

  @Test
  public void lanzarExcepcionSiPersonaHumanaNoTieneCorreoElectronico() {
    assertThrows(DomainValidationException.class, () -> {
      PersonaHumana persona = new PersonaHumanaBuilder().conNombre("Esteban").build();
    });
  }
}
