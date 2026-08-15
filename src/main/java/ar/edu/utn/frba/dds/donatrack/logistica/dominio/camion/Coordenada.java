package ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion;

import lombok.Getter;

@Getter
public class Coordenada {
  private String latitud;
  private String longitud;

  public Coordenada(String latitud, String longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

}
