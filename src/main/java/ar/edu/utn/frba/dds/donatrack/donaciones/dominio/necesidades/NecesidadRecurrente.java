package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;

public class NecesidadRecurrente extends Necesidad {
  private Integer cantidadPorPeriodo;
  private Periodo periodo;

  public NecesidadRecurrente(Subcategoria subcategoria,
                             String descripcion,
                             int cantidadPorPeriodo,
                             Periodo periodo) {
    super(subcategoria, descripcion);
    this.cantidadPorPeriodo = cantidadPorPeriodo;
    this.periodo = periodo;
  }

  public Integer getCantidadPorPeriodo() {
    return cantidadPorPeriodo;
  }

  public Periodo getPeriodo() {
    return periodo;
  }

  public void actualizarDatos(Subcategoria subcategoria,
                              String descripcion,
                              int cantidadPorPeriodo,
                              Periodo periodo) {
    super.actualizarDatos(subcategoria, descripcion);
    this.cantidadPorPeriodo = cantidadPorPeriodo;
    this.periodo = periodo;
  }

  @Override
  public Boolean estaSatisfecha() {
    return getCantidadRecibida() >= cantidadPorPeriodo;
  }

  @Override
  protected Integer getCantidad() {
    return this.cantidadPorPeriodo;
  }
}

