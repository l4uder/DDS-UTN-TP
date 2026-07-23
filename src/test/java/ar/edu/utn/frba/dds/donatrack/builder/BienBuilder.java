package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import java.time.LocalDate;

public class BienBuilder {
  private String descripcion;
  private float cantidad;
  private Subcategoria subcategoria;
  private String foto;
  private LocalDate fechaVencimiento;;
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

  public BienBuilder conFechaVencimiento(LocalDate fecha) {
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
    if (fechaVencimiento == null) {
      throw new DomainValidationException("es necesario saber La fecha de vencimiento, para ser perecedero.");
    }

    return Bien.crearPerecedero(descripcion, cantidad, unidad, foto, subcategoria, fechaVencimiento);
  }

  public Bien buildNoPerecedero() {
    if (usado == null) {
      throw new DomainValidationException("es necesario saber si es usado o no, para ser No perecedero.");
    }

    return Bien.crearNoPerecedero(descripcion, cantidad, UnidadMedida.UNIDADES, foto, subcategoria, usado);
  }
}