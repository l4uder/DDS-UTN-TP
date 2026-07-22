package ar.edu.utn.frba.dds.donatrack.logistica.broker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GpsMensaje {
  private String id;
  private String nivelBateria;
  private String latitud;
  private String longitud;

}
