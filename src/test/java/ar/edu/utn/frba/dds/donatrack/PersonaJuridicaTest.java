package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.builder.PersonaJuridicaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.RepresentanteBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Juridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersonaJuridicaTest {
  Representante representante;

  @BeforeEach
  void configInicial() {
    representante = new RepresentanteBuilder()
        .conNombre("representanteA")
        .conAgregarContacto(new CorreoDeContato("empresaTest@gmail.com", true))
        .build();
  }

  @Test
  public void sePuedeCrearUnaPersonaJuridicaConUnRepresentante() {
    Donante empresa = new PersonaJuridicaBuilder()
        .conDocumento(new Documento(TipoDocumento.CUIT, "30-12345678-9"))
        .conAgregarRepresetante(representante)
        .conRazonSocial("EmpresaSRL")
        .build();

    Juridica tipoJuridica = (Juridica) empresa.getTipoDonante();

    assertEquals(1, tipoJuridica.getRepresentantes().size());
    assertEquals(representante, tipoJuridica.getRepresentantes().get(0));
  }

}
