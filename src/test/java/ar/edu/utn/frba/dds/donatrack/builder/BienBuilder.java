package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donacion.Bien;
import ar.edu.utn.frba.dds.donatrack.donacion.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.Perecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donacion.Subcategoria;

import java.time.LocalDateTime;

public class BienBuilder {
  private String descripcion;
  private float cantidad;
  private Subcategoria subcategoria;
  private String foto;
  private LocalDateTime fechaVencimiento;// = LocalDateTime.now().plusMonths(6);
  private Boolean usado;
  private UnidadMedida unidad;

  public BienBuilder conDescripcion(String descripcion) {
    this.descripcion = descripcion;
    return this;
  }

  public BienBuilder conCantidad(float cantidad) {
    this.cantidad = cantidad;
    return this;
  }

  public BienBuilder conSubcategoria(Subcategoria subcategoria) {
    this.subcategoria = subcategoria;
    return this;
  }

  public BienBuilder conFoto(String foto) {
    this.foto = foto;
    return this;
  }

  public BienBuilder conFechaVencimiento(LocalDateTime fecha) {
    this.fechaVencimiento = fecha;
    return this;
  }

  public BienBuilder conUsado(Boolean estado) {
    this.usado = estado;
    return this;
  }

  public BienBuilder conUnidad(UnidadMedida unidad) {
    this.unidad = unidad;
    return this;
  }

  public Bien buildPerecedero() {
    return Bien.crearPerecedero(descripcion, cantidad, unidad, foto, subcategoria, fechaVencimiento);
  }

  public Bien buildNoPerecedero() {
    return Bien.crearNoPerecedero(descripcion, cantidad, UnidadMedida.UNIDADES, foto, subcategoria, usado);
  }
}