package ar.edu.utn.frba.dds.donatrack.dominio.donacion;

import java.time.LocalDateTime;

public class EstadoDonacion {
  private String detalle;
  private TipoEstadoDonacion tipoEstado;
  private LocalDateTime fecha;

  public EstadoDonacion(TipoEstadoDonacion estado, String observacion) {
    this.tipoEstado = estado;
    this.fecha = LocalDateTime.now();
    this.detalle = (observacion == null || observacion.isBlank()) ? null : observacion;
  }

  public EstadoDonacion(TipoEstadoDonacion estado) {
    this(estado, null);
  }

  public TipoEstadoDonacion getTipoEstado() {
    return tipoEstado;
  }

  public LocalDateTime getFecha() {
    return this.fecha;
  }

  public  String getDetalle() {
    return this.detalle;
  }

  public void setFecha(LocalDateTime fecha) {
    this.fecha = fecha;
  }
}