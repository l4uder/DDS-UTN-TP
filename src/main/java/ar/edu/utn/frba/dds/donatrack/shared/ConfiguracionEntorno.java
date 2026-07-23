package ar.edu.utn.frba.dds.donatrack.shared;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.ConfigurationEnvException;
import io.github.cdimascio.dotenv.Dotenv;

public class ConfiguracionEntorno {
  private static final ConfiguracionEntorno INSTANCE = new ConfiguracionEntorno();
  private final Dotenv dotenv;

  private ConfiguracionEntorno() {
    this.dotenv = Dotenv.load();
  }

  public static ConfiguracionEntorno getInstance() {
    return INSTANCE;
  }

  public String getEmailUsuario() {
    return obtenerObligatorio("EMAIL_USER");
  }

  public String getPasswordUsuario() {
    return obtenerObligatorio("EMAIL_PASSWORD");
  }

  public Integer puertoDonaciones(int puertoPorDefecto) {
    return elegirPuerto("PUERTO_DONACIONES", puertoPorDefecto);
  }

  public Integer puertoLogistica(int puertoPorDefecto) {
    return elegirPuerto("PUERTO_LOGISTICA", puertoPorDefecto);
  }

  //=================== FUNCIONES AUXILIARES =====================
  private String obtenerObligatorio(String nombre) {
    String clave = dotenv.get(nombre);
    if (clave == null || clave.isBlank()) {
      throw new ConfigurationEnvException("Falta la variable de entorno: '" + nombre + "'");
    }
    return clave;
  }

  private int elegirPuerto(String nombre, int valorPorDefecto) {
    String valor = dotenv.get(nombre);
    if (valor == null || valor.isBlank()) {
      System.out.println("Falta la variable de entorno: '" + nombre + "' Se va a usar el puerto por default: " + valorPorDefecto);
      return valorPorDefecto;
    }
    try {
      return Integer.parseInt(valor);
    } catch (NumberFormatException e) {
      System.out.println("Variable de entorno: '" + nombre + "' debe ser un numero, se usará el puerto default: " + valorPorDefecto);
      return valorPorDefecto;
    }
  }

}
