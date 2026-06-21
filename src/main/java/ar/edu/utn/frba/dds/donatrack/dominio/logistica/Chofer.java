package ar.edu.utn.frba.dds.donatrack.dominio.logistica;

public class Chofer {
  private String nombre;
  private String apellido;
  private String licenciaConducir;

  public Chofer(String nombre, String apellido, String licenciaConducir) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.licenciaConducir = licenciaConducir;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public String getLicenciaConducir() {
    return licenciaConducir;
  }
}