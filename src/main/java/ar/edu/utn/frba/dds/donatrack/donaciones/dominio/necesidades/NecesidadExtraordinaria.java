package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;

public class NecesidadExtraordinaria extends Necesidad {
  private Integer cantidadRequerida;

  public NecesidadExtraordinaria(Subcategoria subcategoria,
                                 String descripcion,
                                 int cantidadRequerida) {
    super(subcategoria, descripcion);
    this.cantidadRequerida = cantidadRequerida;
  }

  public Integer getCantidadRequerida() {
    return cantidadRequerida;
  }

  public void actualizarDatos(Subcategoria subcategoria,
                              String descripcion,
                              int cantidadRequerida) {
    super.actualizarDatos(subcategoria, descripcion);
    this.cantidadRequerida = cantidadRequerida;
  }

  @Override
  public Boolean estaSatisfecha() {
    return getCantidadRecibida() >= cantidadRequerida;
  }

  @Override
  protected Integer getCantidad() {
    return this.cantidadRequerida;
  }
}
