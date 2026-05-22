package ar.edu.utn.frba.dds.donatrack.necesidades;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;

public class NecesidadExtraordinaria extends Necesidad {
  private int cantidadRequerida;

  public NecesidadExtraordinaria(Subcategoria subcategoria,
                                 String descripcion,
                                 int cantidadRequerida) {
    super(subcategoria, descripcion);
      this.cantidadRequerida = cantidadRequerida;
  }

  @Override
  public Boolean esSatisfecha() {
    return getCantidadRecibida() >= cantidadRequerida;
  }
}
