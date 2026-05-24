package ar.edu.utn.frba.dds.donatrack.necesidades;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;

public abstract class Necesidad {
  private Subcategoria subcategoria;
  private String descripcion;
  private int cantidadRecibida;

  public Necesidad(Subcategoria subcategoria, String descripcion) {
    this.subcategoria = subcategoria;
    this.descripcion = descripcion;
    this.cantidadRecibida = 0;
  }

  public void recibirBienes(int cantidad) {
    this.cantidadRecibida += cantidad;
  }

  public int getCantidadRecibida() {
    return cantidadRecibida;
  }

  public abstract Boolean esSatisfecha();
}
