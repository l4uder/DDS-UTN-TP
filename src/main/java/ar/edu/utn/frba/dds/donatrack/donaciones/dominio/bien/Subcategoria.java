package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "subcategorias")
public class Subcategoria {
  @Id @GeneratedValue
  @Column (name = "id_subcategoria")
  private Long id;
  @Column (name = "nombre")
  private String nombre;
  @ManyToOne
  @JoinColumn(name = "id_categoria")
  private Categoria categoria;

  public Subcategoria(String nombre, Categoria categoria) {
    this.nombre = nombre.toLowerCase();
    this.categoria = categoria;
  }

  public Boolean esIgual(Subcategoria otraSubcategoria) {
    return this.nombre.equalsIgnoreCase(otraSubcategoria.getNombre())
          && this.categoria.esIgual(otraSubcategoria.getCategoria());
  }

}
