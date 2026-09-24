package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "categorias")
public class Categoria {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_categoria")
  private Long id;
  @Column(name = "nombre")
  private String nombre;

  public Categoria(String nombre) {
    this.nombre = nombre;
  }

  public Boolean esIgual(Categoria categoria) {
    return this.nombre.equalsIgnoreCase(categoria.getNombre());
  }

}
