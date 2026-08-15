package ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta;

import lombok.Getter;

@Getter
public class Chofer {
  private String nombre;
  private String apellido;
  private String licenciaConducir;

  public Chofer(String nombre, String apellido, String licenciaConducir) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.licenciaConducir = licenciaConducir;
  }

}