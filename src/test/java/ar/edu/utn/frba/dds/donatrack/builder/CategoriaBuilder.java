package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Categoria;

public class CategoriaBuilder {
  private String nombre = "Categoria";

  public CategoriaBuilder conNombre(String nombre){
    this.nombre = nombre;
    return this;
  }

  public Categoria build(){
    return new Categoria(nombre);
  }
}
