package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.builder.PersonaJuridicaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.RepresentanteBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersonaJuridicaTest {

  @Test
  public void sePuedeCrearUnaPersonaJuridicaConDatosValidos() {
    PersonaJuridica empresa = new PersonaJuridicaBuilder()
        .conRazonSocial("Empresa Test S.A.")
        .conDocumento(new Documento(TipoDocumento.CUIT, "30-12345678-9"))
        .conAgregarContacto(new CorreoDeContato("empresaTest@gmail.com", true))
        .build();

    assertNotNull(empresa.getPrimerContactoPrincipal());
  }

  @Test
  public void sePuedeAgregarUnRepresentante() {
    PersonaJuridica empresa = new PersonaJuridicaBuilder()
        .conDocumento(new Documento(TipoDocumento.CUIT, "30-12345678-9"))
        .conAgregarContacto(new CorreoDeContato("empresaTest@gmail.com", true))
        .conRazonSocial("EmpresaSRL")
        .build();

    Representante representante = new RepresentanteBuilder()
        .conDocumento(new Documento(TipoDocumento.DNI, "235325554"))
        .conNombre("Marcelo").build();

    empresa.agregarRepresentante(representante);

    assertEquals(1, empresa.getRepresentantes().size());
    assertEquals(representante, empresa.getRepresentantes().get(0));
  }

}
