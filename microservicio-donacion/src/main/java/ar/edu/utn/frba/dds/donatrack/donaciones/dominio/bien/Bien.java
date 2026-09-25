package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "bienes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_bien")
public abstract class Bien {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column (name = "id_bien")
  private Long id;
  @Column (name = "descripcion")
  private String descripcion;
  @Column (name = "cantidad")
  private float cantidad;
  @Column (name = "unidad_medida")
  @Enumerated(EnumType.STRING)
  private UnidadMedida unidadMedida;
  @Column (name = "foto")
  private String foto;
  @Setter
  @ManyToOne
  @JoinColumn(name = "id_subcategoria")
  private Subcategoria subcategoria;

  protected Bien(String descripcion, Float cantidad, UnidadMedida unidad,
               String foto, Subcategoria subcategoria) {
    checkDatos(unidad, cantidad, subcategoria);
    this.descripcion = descripcion;
    this.cantidad = cantidad;
    this.unidadMedida = unidad;
    this.foto = foto;
    this.subcategoria = subcategoria;
  }

  private void checkDatos(UnidadMedida unidadMedida, Float cantidad, Subcategoria subcategoria) {
    if (unidadMedida == null)
      throw new DominioException("El campo 'unidad_medida' es obligatorio, en el Bien");

    if (cantidad == null || cantidad < 0.0)
      throw new DominioException("El campo 'cantidad' es obligatorio y debe ser positivo, en el Bien");

    if (subcategoria == null)
      throw new DominioException("El campo 'subcategoria' es obligatorio, en el Bien");
  }

  public abstract String getNombreClave();

  public abstract String getTipo(); // Esto podría pasar a ser un enum

  public float getCantidadMenorMedida() {
    return this.unidadMedida.convertirAMenorMedida(cantidad);
  }

}