package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import org.junit.jupiter.api.Test;

public class PersonaHumanaTest {
  MedioContacto contactoPred = new MedioContacto(TipoContacto.CORREO, "estebancarp@gmail.com");

  @Test
  public void sePuedeCrearUnaPersonaHumanaConDatosValidos() {
    PersonaHumana persona = new PersonaHumanaBuilder()
        .conNombre("Esteban")
        .conContactoPredeterminado(contactoPred)
        .build();

    assertNotNull(persona.getMedioDeContactoPred());
    assertEquals("Esteban", "Esteban");
  }

  @Test
  public void lanzarExcepcionSiPersonaHumanaNoTieneCorreoElectronico() {
    assertThrows(IllegalArgumentException.class, () -> {
      new PersonaHumanaBuilder()
          .vaciarContactos()
          .conContactoPredeterminado(new MedioContacto(TipoContacto.TELEFONO, "11223344"))
          .build();
    });
  }
}