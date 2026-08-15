package ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class EstadoEntrega {
  private LocalDateTime fecha;
  private String detalle;
  private TipoEstadoEntrega tipoEstado;
  private Camion camion;

  public EstadoEntrega(TipoEstadoEntrega estado, String observacion, Camion camion) {
    this.tipoEstado = estado;
    this.fecha = LocalDateTime.now();
    this.detalle = (observacion == null || observacion.isBlank()) ? null : observacion;
    this.camion = camion;
  }

  public EstadoEntrega(TipoEstadoEntrega estado, Camion camion) {
    this(estado, null, camion);
  }

}
