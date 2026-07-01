package ar.edu.utn.frba.dds.donatrack.dominio.logistica;

import java.time.LocalDateTime;

public class EstadoEntrega {
  private LocalDateTime fecha;
  private TipoEstadoEntrega tipoEstado;
  private String detalle;

  public EstadoEntrega(TipoEstadoEntrega estado, String observacion) {
    this.tipoEstado = estado;
    this.fecha = LocalDateTime.now();
    this.detalle = (observacion == null || observacion.isBlank()) ? null : observacion;
  }

  public EstadoEntrega(TipoEstadoEntrega estado) {
    this(estado, null);
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
