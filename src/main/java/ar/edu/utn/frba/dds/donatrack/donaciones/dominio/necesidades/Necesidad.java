package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;

public abstract class Necesidad {
  private String id;
  private Subcategoria subcategoria;
  private String descripcion;
  private Integer cantidadRecibida;

  public Necesidad(Subcategoria subcategoria, String descripcion) {
    this.subcategoria = subcategoria;
    this.descripcion = descripcion;
    this.cantidadRecibida = 0;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Subcategoria getSubcategoria() {
    return subcategoria;
  }

  public String getDescripcion() {
    return descripcion;
  }

  protected void actualizarDatos(Subcategoria subcategoria, String descripcion) {
    this.subcategoria = subcategoria;
    this.descripcion = descripcion;
  }

  public void recibirBienes(Integer cantidad) {
    this.cantidadRecibida += cantidad;
  }

  public Integer getCantidadRecibida() {
    return cantidadRecibida;
  }

  public abstract Boolean esSatisfecha();
}
