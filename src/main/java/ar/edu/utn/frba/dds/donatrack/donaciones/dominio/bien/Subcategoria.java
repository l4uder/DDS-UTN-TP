package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;

import lombok.Getter;

@Getter
public class Subcategoria {
  private String nombre;
  private Categoria categoria;

  public Subcategoria(String nombre,
                      Categoria categoria) {
    this.nombre = nombre;
    this.categoria = categoria;
  }

  public Boolean esIgual(Subcategoria otraSubcategoria) {
    return this.nombre.equalsIgnoreCase(otraSubcategoria.getNombre())
          && this.categoria.esIgual(otraSubcategoria.getCategoria());
  }

}