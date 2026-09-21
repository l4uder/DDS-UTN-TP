package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "estados_donacion")
public class EstadoDonacion {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
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