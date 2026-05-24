package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donacion.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.Perecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;

import java.time.LocalDateTime;

public class BienBuilder {

  private String descripcion = "Bien default";
  private float cantidad = 1;
  private Subcategoria subcategoria = new SubcategoriaBuilder().build();
  private byte[] foto = null;
  private LocalDateTime fechaVencimiento = LocalDateTime.now().plusMonths(6);
  private Boolean usado = false;

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

  public BienBuilder conFechaVencimiento(LocalDateTime fecha) {
    this.fechaVencimiento = fecha;
    return this;
  }

  public BienBuilder usado() {
    this.usado = true;
    return this;
  }

  public Perecedero buildPerecedero(UnidadMedida unidad) {
    return new Perecedero(descripcion, cantidad, unidad, foto, subcategoria, fechaVencimiento);
  }

  public NoPerecedero buildNoPerecedero() {
    return new NoPerecedero(descripcion, cantidad, UnidadMedida.UNIDADES, foto, subcategoria, usado);
  }
}