package ar.edu.utn.frba.dds.donatrack.necesidades;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;

public class NecesidadRecurrente extends Necesidad{
  private int cantidadPorPeriodo;
  private int cantidadRecibida;
  private Periodo periodo;

  public NecesidadRecurrente(Subcategoria subcategoria,
                             String descripcion,
                             int cantidadPorPeriodo,
                             int cantidadRecibida,
                             Periodo periodo){
    super(subcategoria, descripcion);
    this.cantidadPorPeriodo=cantidadPorPeriodo;
    this.cantidadRecibida=cantidadRecibida;
    this.periodo=periodo;
  }

  @Override

  public Boolean esSatisfecha(){
    return  this.cantidadRecibida >= this.cantidadPorPeriodo;
  }
}

