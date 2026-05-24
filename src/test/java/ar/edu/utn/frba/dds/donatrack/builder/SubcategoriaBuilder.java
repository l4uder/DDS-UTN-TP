package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Categoria;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;

public class SubcategoriaBuilder {
  private String nombre = "Subcategoria";
  private Categoria categoria = new CategoriaBuilder().build();

  public SubcategoriaBuilder conNombre(String nombre){
    this.nombre = nombre;
    return this;
  }

  public SubcategoriaBuilder conCategoria(Categoria categoria){
    this.categoria = categoria;
    return this;
  }

  public Subcategoria build(){
    return new Subcategoria(nombre, categoria);
  }
}
