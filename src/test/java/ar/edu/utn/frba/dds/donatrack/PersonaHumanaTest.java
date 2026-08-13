package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.persona.Humana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import org.junit.jupiter.api.Test;

public class PersonaHumanaTest {

  @Test
  public void sePuedeCrearUnaPersonaHumanaConDatosValidos() {
    Donante persona = new PersonaHumanaBuilder()
        .conNombre("Esteban")
        .conDocumento(new Documento(TipoDocumento.DNI, "45123456"))
        .conAgregarContacto(new CorreoDeContato("estebancarp@gmail.com", true))
        .conDireccion("alguna dirección")
        .build();

    Humana tipoHumana = (Humana) persona.getTipoDonante();

    assertNotNull(persona.getPrimerContactoPrincipal());
    assertEquals("Esteban", tipoHumana.getNombre());
  }

  @Test
  public void lanzarExcepcionSiPersonaHumanaNoTieneCorreoElectronico() {
    assertThrows(DominioException.class, () -> {
      Donante persona = new PersonaHumanaBuilder().conNombre("Esteban").build();
    });
  }
}
