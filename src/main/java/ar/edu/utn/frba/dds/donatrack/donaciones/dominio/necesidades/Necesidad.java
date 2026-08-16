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
  private Integer cantidadRequerida;

  public Necesidad(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, Integer cantidadRequerida) {
    checkDatosBase(subcategoria, unidadMedida, descripcion, cantidadRequerida);
    this.subcategoria = subcategoria;
    this.unidadMedida = unidadMedida;
    this.descripcion = descripcion;
    this.cantidadRecibida = 0;
    this.cantidadRequerida = cantidadRequerida;
  }

  private void checkDatosBase(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, Integer cantidadRequerida) {
    if (subcategoria == null || subcategoria.getNombre().isBlank()) {
      throw new DominioException("El campo 'subcategoria' es obligatorio");
    }
    if (unidadMedida == null) {
      throw new DominioException("El campo 'unidad_Medida' es obligatorio");
    }
    if (descripcion == null || descripcion.isBlank()) {
      throw new DominioException("El campo 'descripcion' es obligatorio");
    }
    if (cantidadRequerida == null || cantidadRequerida <= 0) {
      throw new DominioException("Una necesidad extraordinaria necesita 'cantidad_requerida' mayor a cero");
    }
  }

  abstract public String getTipo();

  public float getCantidadFaltanteEnMenorMedida() {
    return this.unidadMedida.convertirAMenorMedida(this.getCantidadFaltante());
  }

  public void recibirBienes(Integer cantidad) {
    this.cantidadRecibida += cantidad;
  }

  public Boolean estaSatisfecha() {
    return this.cantidadRecibida >= this.cantidadRequerida;
  }

  protected void actualizarDatosBase(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, Integer cantidadRequerida) {
    checkDatosBase(subcategoria, unidadMedida, descripcion, cantidadRequerida);
    this.subcategoria = subcategoria;
    this.unidadMedida = unidadMedida;
    this.descripcion = descripcion;
    this.cantidadRequerida = cantidadRequerida;
  }

  //==================== FUNCIONES AUXILIARES =======================
  private Integer getCantidadFaltante() {
    int cantidadFaltante = this.cantidadRequerida - this.cantidadRecibida;
    return Math.max(0, cantidadFaltante);
  }

}
