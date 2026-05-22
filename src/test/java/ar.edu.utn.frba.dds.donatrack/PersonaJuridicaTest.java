package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.donatrack.builder.PersonaJuridicaBuilder;
import ar.edu.utn.frba.dds.donatrack.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class PersonaJuridicaTest {

  @Test
  public void sePuedeCrearUnaPersonaJuridicaConDatosValidos() {
    PersonaJuridica empresa = new PersonaJuridicaBuilder()
        .conRazonSocial("Empresa Test S.A.")
        .conEmail("empresaTest@gmail.com")
        .build();

    assertNotNull(empresa.getMedioDeContactoPred());
  }

  @Test
  public void lanzarExcepcionSiPersonaJuridicaNoTieneRepresentantes() {
    assertThrows(IllegalArgumentException.class, () -> {
      new PersonaJuridicaBuilder()
          .conRepresentantes(new ArrayList<>())
          .build();
    });
  }
}
