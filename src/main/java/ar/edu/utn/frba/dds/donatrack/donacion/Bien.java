package ar.edu.utn.frba.dds.donatrack.donacion;

public abstract class Bien {
  protected String descripcion;
  protected float cantidad;
  protected UnidadMedida unidad;
  protected String foto;
  protected Subcategoria subcategoria;

  public Bien(String descripcion, float cantidad,
              UnidadMedida unidad, String foto,
              Subcategoria subcategoria) {

    this.descripcion = descripcion;
    this.cantidad = cantidad;
    this.unidad = unidad;
    this.foto = foto;
    this.subcategoria = subcategoria;
  }

  public String getDescripcion() {
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

  abstract public String getNombreClave();
}