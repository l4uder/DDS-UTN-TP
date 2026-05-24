package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.donante.PersonaHumana;
import org.junit.jupiter.api.Test;

public class PersonaHumanaTest {
  @Test
  public void sePuedeCrearUnaPersonaHumanaConDatosValidos() {
    PersonaHumana persona = new PersonaHumanaBuilder()
        .conNombre("Esteban")
        .conEmail("estebancarp@gmail.com")
        .build();

    assertNotNull(persona.getMedioDeContactoPred());
    assertEquals("Esteban", "Esteban");
  }

  @Test
  public void lanzarExcepcionSiPersonaHumanaNoTieneCorreoElectronico() {
    assertThrows(DomainValidationException.class, () -> {
      new PersonaHumanaBuilder()
          .vaciarContactos()
          .build();
    });
  }
}
