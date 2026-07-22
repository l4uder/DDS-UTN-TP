package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import io.github.cdimascio.dotenv.Dotenv;

public class FabricaClienteCorreoReal {

    public static ClienteCorreoReal start() {
      Dotenv dotenv = Dotenv.load();
      String correo = dotenv.get("EMAIL_USER");
      String password = dotenv.get("EMAIL_PASSWORD");

      if (correo == null || correo.isEmpty()) throw new DomainValidationException("Agregue EMAIL_USER a las variables de entorno y verifique que este correcto");
      if (password == null || password.isEmpty()) throw new DomainValidationException("Agregue EMAIL_PASSWORD a las variables de entorno y verifique que este correcto");

      return new ClienteCorreoReal(correo, password);
    }

}