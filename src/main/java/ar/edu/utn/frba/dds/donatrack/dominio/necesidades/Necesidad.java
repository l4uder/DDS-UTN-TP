package ar.edu.utn.frba.dds.donatrack.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.dominio.bien.Subcategoria;

public abstract class Necesidad {
  private Subcategoria subcategoria;
  private String descripcion;
  private Integer cantidadRecibida;

  public Necesidad(Subcategoria subcategoria, String descripcion) {
    this.subcategoria = subcategoria;
    this.descripcion = descripcion;
    this.cantidadRecibida = 0;
  }

  public void recibirBienes(Integer cantidad) {
    this.cantidadRecibida += cantidad;
  }

  public Integer getCantidadRecibida() {
    return cantidadRecibida;
  }

  public abstract Boolean esSatisfecha();
}
