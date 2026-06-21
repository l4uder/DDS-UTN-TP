package ar.edu.utn.frba.dds.donatrack.dominio.logistica;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CambioEstadoEntrega {
  private Entrega entrega;
  private LocalDateTime fecha;
  private TipoEstadoEntrega tipoEstado;
  private String detalle;
  private Camion camionResponsable;

  public CambioEstadoEntrega(Entrega entrega, TipoEstadoEntrega estado,
                             String observacion, Camion camion) {
    this.entrega = entrega;
    this.fecha = LocalDateTime.now();
    this.tipoEstado = estado;
    this.detalle = observacion;
    this.camionResponsable = camion;
  }

  public CambioEstadoEntrega(Entrega entrega, TipoEstadoEntrega estado, Camion camion) {
    this(entrega, estado, null, camion);
  }

  public Entrega getEntrega() {
    return this.entrega;
  }

  public TipoEstadoEntrega getTipoEstado() {
    return this.tipoEstado;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }

  public String getDetalle() {
    return detalle;
  }

  public Camion getCamionResponsable() {
    return camionResponsable;
  }
}
