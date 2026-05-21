package ar.edu.utn.frba.dds.donatrack.donacion;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;

public abstract class Bien {
  protected String descripcion;
  protected float cantidad;
  protected UnidadMedida unidad;
  protected byte[] foto;
  protected Subcategoria subcategoria;

  public Bien(String descripcion, float cantidad,
              UnidadMedida unidad, byte[] foto,
              Subcategoria subcategoria) {

    this.descripcion = descripcion;
    this.cantidad = cantidad;
    this.unidad = unidad;
    this.foto = foto != null ? foto.clone() : null;
    this.subcategoria = subcategoria;
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public String getNombre() {
    return this.descripcion;
  }

  public float getCantidad() {
    return this.cantidad;
  }

  public UnidadMedida getUnidadMedida() {
    return this.unidad;
  }

  public Subcategoria getSubcategoria() {
    return this.subcategoria;
  }
}