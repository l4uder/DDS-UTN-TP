package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo;

import ar.edu.utn.frba.dds.donatrack.shared.ConfiguracionEntorno;

public class FabricaClienteCorreoReal {

    public static ClienteCorreoReal start() {
      ConfiguracionEntorno config = ConfiguracionEntorno.getInstance();
      String correo = config.getEmailUsuario();
      String password = config.getPasswordUsuario();

      return new ClienteCorreoReal(correo, password);
    }

}