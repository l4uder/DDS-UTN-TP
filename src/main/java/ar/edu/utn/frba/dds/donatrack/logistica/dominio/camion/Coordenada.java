package ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Coordenada {
  @Column(name = "latitud")
  private String latitud;
  @Column(name = "longitud")
  private String longitud;

  public Coordenada(String latitud, String longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }
}
