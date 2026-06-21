package ar.edu.utn.frba.dds.donatrack.dominio.logistica;

public class Camion {
  private String patente;
  private float capacidadVolumen;
  private float altura;
  private float capacidadCarga;

  public Camion(String patente, float capacidadVolumen,
                float altura, float capacidadCarga) {
    this.patente = patente;
    this.capacidadVolumen = capacidadVolumen;
    this.altura = altura;
    this.capacidadCarga = capacidadCarga;
  }
}
