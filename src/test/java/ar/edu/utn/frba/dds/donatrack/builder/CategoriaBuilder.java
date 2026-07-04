package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;

public class CategoriaBuilder {
  private String nombre;

  public CategoriaBuilder conNombre(String nombre){
    this.nombre = nombre;
    return this;
  }

  public Categoria build(){
    if (this.nombre == null || this.nombre.trim().isEmpty()) {
      throw new IllegalStateException("No se puede construir una Subcategoria sin un nombre válido.");
    }

    return new Categoria(nombre);
  }
}
