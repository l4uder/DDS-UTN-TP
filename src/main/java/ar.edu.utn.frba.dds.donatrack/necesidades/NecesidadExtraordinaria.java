package ar.edu.utn.frba.dds.donatrack.necesidades;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;

public class NecesidadExtraordinaria extends Necesidad{
  private int cantidadRequerida;
  private int cantidadRecibida;

  public NecesidadExtraordinaria(Subcategoria subcategoria,
                                 String descripcion,
                                 int cantidadRecibida,
                                 int cantidadRequerida){
    super(subcategoria, descripcion);
    this.cantidadRecibida = cantidadRecibida;
    this.cantidadRequerida = cantidadRequerida;
  }
  @Override
  public Boolean esSatisfecha(){
    return cantidadRecibida >= cantidadRequerida;
  }
}
