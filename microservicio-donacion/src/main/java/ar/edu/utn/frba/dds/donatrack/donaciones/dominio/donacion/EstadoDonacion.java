package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Embeddable
public class EstadoDonacion {
  @Column(name = "detalle")
  private String detalle;
  @Column(name = "tipo_estado")
  @Enumerated(EnumType.STRING)
  private TipoEstadoDonacion tipoEstado;
  @Setter
  @Column (name = "fecha")
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