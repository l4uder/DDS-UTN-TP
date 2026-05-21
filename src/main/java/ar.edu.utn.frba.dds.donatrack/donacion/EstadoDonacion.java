package ar.edu.utn.frba.dds.donatrack.donacion;

import java.time.LocalDateTime;

public class EstadoDonacion {
  private LocalDateTime fecha;
  private TipoEstadoDonacion tipoEstado;
  private String detalle;

  public EstadoDonacion(TipoEstadoDonacion estado, String observacion) {
    if (estado == TipoEstadoDonacion.ENTREGA_FALLIDA && (observacion == null || observacion.isBlank())) {
        throw new IllegalArgumentException("Se requiere justificación para entrega fallida");
    }
    this.tipoEstado = estado;
    this.fecha = LocalDateTime.now();
    this.detalle = observacion;
  }

  public EstadoDonacion(TipoEstadoDonacion estado) {
      this(estado, null);
  }

  public TipoEstadoDonacion getTipoEstado() {
      return tipoEstado;
  }
}