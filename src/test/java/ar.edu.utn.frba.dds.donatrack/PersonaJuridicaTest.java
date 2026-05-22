package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.donatrack.builder.PersonaJuridicaBuilder;
import ar.edu.utn.frba.dds.donatrack.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class PersonaJuridicaTest {
  MedioContacto contactoPred = new MedioContacto(TipoContacto.CORREO, "empresaTest@gmail.com");

  @Test
  public void sePuedeCrearUnaPersonaJuridicaConDatosValidos() {
    PersonaJuridica empresa = new PersonaJuridicaBuilder()
        .conRazonSocial("Empresa Test S.A.")
        .conMedioDeContacto(contactoPred)
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