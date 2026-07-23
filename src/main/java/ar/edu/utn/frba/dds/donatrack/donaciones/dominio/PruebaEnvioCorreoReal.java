package ar.edu.utn.frba.dds.donatrack.donaciones.dominio;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo.FabricaClienteCorreoReal;
import java.util.List;

public class PruebaEnvioCorreoReal {

  public static void main(String[] args) {
    CorreoDeContato correo = new CorreoDeContato("ericleohuanto@gmail.com", true);
    PersonaHumana persona = new PersonaHumana("usuario",
        null,
        new Documento(TipoDocumento.DNI, "33333"),
        null,
        Genero.MASCULINO,
        "Alguna dirección",
        List.of(correo));

    persona.recibirNotificacion("mensaje de prueba");
    correo.setClienteCorreo(FabricaClienteCorreoReal.start());
    persona.recibirNotificacion("mensaje nuevooooo");
  }

}
