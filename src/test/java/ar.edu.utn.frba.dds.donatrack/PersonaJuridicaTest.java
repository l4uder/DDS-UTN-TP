package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.donatrack.builder.PersonaJuridicaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.RepresentanteBuilder;
import ar.edu.utn.frba.dds.donatrack.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donante.Representante;
import ar.edu.utn.frba.dds.donatrack.donante.TipoDocumento;
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
  public void sePuedeAgregarUnRepresentante() {
    PersonaJuridica empresa = new PersonaJuridicaBuilder().build();
    var representante = new RepresentanteBuilder().conDocumento(TipoDocumento.DNI, "235325554").conNombre("Marcelo").build();
    empresa.agregarRepresentante(representante);

    assertEquals(1, empresa.getRepresentantes().size());
    assertEquals(representante, empresa.getRepresentantes().getFirst());
  }
}
