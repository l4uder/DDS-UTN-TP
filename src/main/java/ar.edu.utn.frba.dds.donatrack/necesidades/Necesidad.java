package ar.edu.utn.frba.dds.donatrack.necesidades;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;

public abstract class Necesidad {
  private Subcategoria subcategoria;
  private String descripcion;

  public Necesidad (Subcategoria subcategoria, String descripcion){
    this.subcategoria = subcategoria;
    this.descripcion = descripcion;
  }

  public abstract Boolean esSatisfecha();
}
