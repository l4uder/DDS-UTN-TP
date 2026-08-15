package ar.edu.utn.frba.dds.donatrack.donaciones.dominio;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo.ClienteCorreoMock;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo.ClienteCorreoReal;
import java.util.List;

public class PruebaEnvioCorreoReal {

  public static void main(String[] args) {
    CorreoDeContato correoFalso = new CorreoDeContato("correoFalso@gmail.com", true, new ClienteCorreoMock());
    CorreoDeContato correoVerdadero = new CorreoDeContato("ericleohuanto@gmail.com", true, new ClienteCorreoReal());

    Donante persona = Donante.personaHumana(
        "usuario",
        null,
        new Documento(TipoDocumento.DNI, "33333"),
        null,
        Genero.MASCULINO,
        "Alguna dirección",
        List.of(correoFalso, correoVerdadero));

    persona.recibirNotificacion("mensaje de prueba");
  }

}
