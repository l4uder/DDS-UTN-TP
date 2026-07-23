package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EstadoDonacion {
  private String detalle;
  private TipoEstadoDonacion tipoEstado;
  @Setter
  private LocalDateTime fecha;

  public EstadoDonacion(TipoEstadoDonacion estado, String observacion) {
    this.tipoEstado = estado;
    this.fecha = LocalDateTime.now();
    this.detalle = (observacion == null || observacion.isBlank()) ? null : observacion;
  }

  public EstadoDonacion(TipoEstadoDonacion estado) {
    this(estado, null);
  }

}