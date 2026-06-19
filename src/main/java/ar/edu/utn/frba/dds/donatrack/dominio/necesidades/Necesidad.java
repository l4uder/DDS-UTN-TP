package ar.edu.utn.frba.dds.donatrack.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.dominio.bien.Subcategoria;

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
