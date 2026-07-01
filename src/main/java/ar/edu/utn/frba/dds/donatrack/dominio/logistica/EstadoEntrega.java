package ar.edu.utn.frba.dds.donatrack.dominio.logistica;

import java.time.LocalDateTime;

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

  public TipoEstadoEntrega getTipoEstado() {
    return this.tipoEstado;
  }

  public LocalDateTime getFecha() {
    return this.fecha;
  }

  public String getDetalle() {
    return this.detalle;
  }
}
