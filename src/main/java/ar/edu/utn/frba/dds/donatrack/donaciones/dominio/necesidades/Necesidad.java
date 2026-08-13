package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Necesidad {
  @Setter
  private String id;
  private Subcategoria subcategoria;
  private UnidadMedida unidadMedida;
  private String descripcion;
  private Integer cantidadRecibida;

  public Necesidad(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion) {
    checkDatosBase(subcategoria, unidadMedida, descripcion);
    this.subcategoria = subcategoria;
    this.unidadMedida = unidadMedida;
    this.descripcion = descripcion;
    this.cantidadRecibida = 0;
  }

  private void checkDatosBase(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion) {
    if (subcategoria == null || subcategoria.getNombre().isBlank()) {
      throw new DominioException("El campo 'subcategoria' es obligatorio");
    }
    if (unidadMedida == null) {
      throw new DominioException("El campo 'unidadMedida' es obligatorio");
    }
    if (descripcion == null || descripcion.isBlank()) {
      throw new DominioException("El campo 'descripcion' es obligatorio");
    }
  }

  public void recibirBienes(Integer cantidad) {
    this.cantidadRecibida += cantidad;
  }

  public abstract Boolean estaSatisfecha();

  protected abstract Integer getCantidadQueNecesita();

  public Integer getCantidadFaltante() {
    int cantidadFaltante = getCantidadQueNecesita() - this.cantidadRecibida;
    return Math.max(0, cantidadFaltante);
  }

  public float getCantidadFaltanteEnMenorMedida() {
    return this.unidadMedida.convertirAMenorMedida(this.getCantidadFaltante());
  }

  abstract public String getTipo();

  protected void actualizarDatosBase(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion) {
    checkDatosBase(subcategoria, unidadMedida, descripcion);
    this.subcategoria = subcategoria;
    this.unidadMedida = unidadMedida;
    this.descripcion = descripcion;
  }

}
