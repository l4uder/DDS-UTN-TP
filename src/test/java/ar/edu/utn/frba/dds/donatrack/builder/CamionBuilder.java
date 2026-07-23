package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Coordenada;
import java.util.ArrayList;
import java.util.List;

public class CamionBuilder {
  private String patente;
  private float capacidadVolumen;
  private float altura;
  private float capacidadCarga;
  private List<Coordenada> coordenadas;

  public CamionBuilder() {
    this.coordenadas = new ArrayList<>();
  }

  public CamionBuilder conPatente(String patente) {
    this.patente = patente;
    return this;
  }

  public CamionBuilder conCapacidadVolumen(float capacidadVolumen) {
    this.capacidadVolumen = capacidadVolumen;
    return this;
  }

  public CamionBuilder conAltura(float altura) {
    this.altura = altura;
    return this;
  }

  public CamionBuilder conCapacidadCarga(float capacidadCarga) {
    this.capacidadCarga = capacidadCarga;
    return this;
  }

  public Camion build() {
    return new Camion(patente, capacidadVolumen, altura, capacidadCarga);
  }
}
