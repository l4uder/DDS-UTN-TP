package ar.edu.utn.frba.dds.donatrack.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.dominio.bien.Subcategoria;

public class NecesidadRecurrente extends Necesidad {
  private int cantidadPorPeriodo;
  private Periodo periodo;

  public NecesidadRecurrente(Subcategoria subcategoria,
                             String descripcion,
                             int cantidadPorPeriodo,
                             Periodo periodo) {
    super(subcategoria, descripcion);
    this.cantidadPorPeriodo = cantidadPorPeriodo;
    this.periodo = periodo;
  }

  @Override
  public Boolean esSatisfecha() {
    return getCantidadRecibida() >= cantidadPorPeriodo;
  }
}

