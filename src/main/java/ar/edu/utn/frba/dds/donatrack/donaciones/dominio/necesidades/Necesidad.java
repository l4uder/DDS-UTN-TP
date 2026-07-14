package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;

public abstract class Necesidad {
  private String id;
  private Subcategoria subcategoria;
  private UnidadMedida unidadMedida;
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

  //Despues quitarlo de aqui y pasarlo por constructor
  public void setUnidadMedida(UnidadMedida unidadMedida) {
    this.unidadMedida = unidadMedida;
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

  public abstract Boolean estaSatisfecha();

  protected abstract Integer getCantidad();

  public Integer getCantidadFaltante() {
    int cantidadFaltante = this.getCantidad() - this.cantidadRecibida;
    return Math.max(0, cantidadFaltante);
  }

  public float getCantidadFaltanteEnMenorMedida() {
    return this.unidadMedida.convertirAMenorMedida(this.getCantidadFaltante());
  }
}
