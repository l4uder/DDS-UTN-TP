package ar.edu.utn.frba.dds.donatrack.dominio.mqtt;

public class GpsMensaje {
  private String id;
  private String latitud;
  private String longitud;

  public GpsMensaje(String id, String latitud, String longitud) {
    this.id = id;
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public String getId() {
    return this.id;
  }

  public String getLatitud() {
    return this.latitud;
  }
  public String getLongitud() {
    return this.longitud;
  }
}
