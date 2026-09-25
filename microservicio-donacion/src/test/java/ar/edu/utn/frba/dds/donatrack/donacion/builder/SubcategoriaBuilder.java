package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;

public class SubcategoriaBuilder {
  private String nombre;
  private Categoria categoria;

  public SubcategoriaBuilder conNombre(String nombre){
    this.nombre = nombre;
    return this;
  }

  public SubcategoriaBuilder conCategoria(Categoria categoria){
    this.categoria = categoria;
    return this;
  }

  public Subcategoria build(){
    if (this.nombre == null || this.nombre.trim().isEmpty()) {
      throw new IllegalStateException("No se puede construir una Subcategoria sin un nombre válido.");
    }
    if (this.categoria == null) {
      throw new IllegalStateException("Toda Subcategoria debe pertenecer a una Categoria.");
    }

    return new Subcategoria(nombre, categoria);
  }
}
