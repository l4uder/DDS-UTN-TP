package ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class EstadoEntrega {
  @Column(name = "fecha")
  private LocalDateTime fecha;
  @Column(name = "detalle")
  private String detalle;
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_estado")
  private TipoEstadoEntrega tipoEstado;
  @ManyToOne
  @JoinColumn(name = "camion_patente")
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
