package ar.edu.utn.frba.dds.donatrack.logistica.dominio;

import java.util.ArrayList;
import java.util.List;

public class Camion {
  private String patente;
  private float capacidadVolumen;
  private float altura;
  private float capacidadCarga;
  private List<Coordenada> coordenadas;
  private Gps gps;

  public Camion(String patente, float capacidadVolumen,
                float altura, float capacidadCarga) {
    this.patente = patente;
    this.capacidadVolumen = capacidadVolumen;
    this.altura = altura;
    this.capacidadCarga = capacidadCarga;
    this.coordenadas = new ArrayList<>();
    this.gps = null;
  }

  public void agregarGps(Gps gps) {
    this.gps = gps;
  }

  public Boolean posee(String idGps) {
    return this.gps.getImei().equalsIgnoreCase(idGps);
  }

  public void agregarCoordenada(Coordenada coordenadas) {
    this.coordenadas.add(coordenadas);
  }

  public Coordenada getUbicacionActual() {
    if (this.coordenadas.isEmpty()) {
      return null;
    }
    return this.coordenadas.get(this.coordenadas.size() - 1);
  }
}
