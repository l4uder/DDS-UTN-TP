package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "necesidades")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_necesidad")
public abstract class Necesidad {
  @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;
  @Transient
  private Subcategoria subcategoria;
  @Column(name = "unidad_medida")
  @Enumerated (EnumType.STRING)
  private UnidadMedida unidadMedida;
  @Column(name = "descripcion")
  private String descripcion;
  @Column(name = "cantidad_recibida")
  private Integer cantidadRecibida;
  @Column(name = "cantidad_requerida")
  private Integer cantidadRequerida;

  public Necesidad(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, Integer cantidadRequerida) {
    checkDatosBase(subcategoria, unidadMedida, descripcion, cantidadRequerida);
    this.subcategoria = subcategoria;
    this.unidadMedida = unidadMedida;
    this.descripcion = descripcion;
    this.cantidadRecibida = 0;
    this.cantidadRequerida = cantidadRequerida;
  }

  private void checkDatosBase(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, Integer cantidadRequerida) {
    if (subcategoria == null || subcategoria.getNombre().isBlank()) {
      throw new DominioException("El campo 'subcategoria' es obligatorio");
    }
    if (unidadMedida == null) {
      throw new DominioException("El campo 'unidad_Medida' es obligatorio");
    }
    if (descripcion == null || descripcion.isBlank()) {
      throw new DominioException("El campo 'descripcion' es obligatorio");
    }
    if (cantidadRequerida == null || cantidadRequerida <= 0) {
      throw new DominioException("Una necesidad extraordinaria necesita 'cantidad_requerida' mayor a cero");
    }
  }

  abstract public String getTipo();

  public float getCantidadFaltanteEnMenorMedida() {
    return this.unidadMedida.convertirAMenorMedida(this.getCantidadFaltante());
  }

  public void recibirBienes(Integer cantidad) {
    this.cantidadRecibida += cantidad;
  }

  public Boolean estaSatisfecha() {
    return this.cantidadRecibida >= this.cantidadRequerida;
  }

  protected void actualizarDatosBase(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, Integer cantidadRequerida) {
    checkDatosBase(subcategoria, unidadMedida, descripcion, cantidadRequerida);
    this.subcategoria = subcategoria;
    this.unidadMedida = unidadMedida;
    this.descripcion = descripcion;
    this.cantidadRequerida = cantidadRequerida;
  }

  //==================== FUNCIONES AUXILIARES =======================
  private Integer getCantidadFaltante() {
    int cantidadFaltante = this.cantidadRequerida - this.cantidadRecibida;
    return Math.max(0, cantidadFaltante);
  }

}
