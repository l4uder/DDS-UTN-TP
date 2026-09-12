package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="Categoria")
public class Categoria {
  @Id @GeneratedValue()
  @Column(name="id_categoria")
  private Long id;
  @Column(name="nombre")
  private String nombre;

  public Categoria(String nombre) {
    this.nombre = nombre;
  }

  public Boolean esIgual(Categoria categoria) {
    return this.nombre.equalsIgnoreCase(categoria.getNombre());
  }

}
