package ar.edu.utn.frba.dds.donatrack.donacion;

import ar.edu.utn.frba.dds.donatrack.builder.PersonaJuridicaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.RepresentanteBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Juridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContacto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonaJuridicaTest {
  Representante representante;

  @BeforeEach
  void configInicial() {
    representante = new RepresentanteBuilder()
        .conNombre("representanteA")
        .conAgregarContacto(new CorreoDeContacto("empresaTest@gmail.com", true))
        .build();
  }

  @Test
  public void sePuedeCrearUnaPersonaJuridicaConUnRepresentante() {
    Juridica empresa = new PersonaJuridicaBuilder()
        .conDocumento(new Documento(TipoDocumento.CUIT, "30-12345678-9"))
        .conAgregarRepresetante(representante)
        .conRazonSocial("EmpresaSRL")
        .build();

    assertEquals(1, empresa.getRepresentantes().size());
    assertEquals(representante, empresa.getRepresentantes().get(0));
  }

}
