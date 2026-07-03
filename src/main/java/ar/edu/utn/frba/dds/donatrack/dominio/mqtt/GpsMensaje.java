package ar.edu.utn.frba.dds.donatrack.dominio.mqtt;

import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Coordenada;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GpsMensaje {
  private String id;
  private String nivelBateria;
  private String latitud;
  private String longitud;

  public GpsMensaje(String id, String nivelBateria, String latitud, String longitud) {
    this.id = id;
    this.nivelBateria = nivelBateria;
    this.latitud = latitud;
    this.longitud = longitud;
  }

}
