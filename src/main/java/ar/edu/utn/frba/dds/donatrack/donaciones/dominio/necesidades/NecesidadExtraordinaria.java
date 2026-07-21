package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import lombok.Getter;

@Getter
public class NecesidadExtraordinaria extends Necesidad {
  private Integer cantidadRequerida;

  public NecesidadExtraordinaria(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, int cantidadRequerida) {
    super(subcategoria, unidadMedida, descripcion);
    checkDatos(cantidadRequerida);
    this.cantidadRequerida = cantidadRequerida;
  }

  private void checkDatos(Integer cantidadRequerida) {
    if (cantidadRequerida == null || cantidadRequerida <= 0) {
      throw new DomainValidationException("Una necesidad extraordinaria necesita 'cantidadRequerida' mayor a cero");
    }
  }

  public void actualizarDatos(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, Integer cantidadRequerida) {
    super.actualizarDatosBase(subcategoria, unidadMedida, descripcion);
    checkDatos(cantidadRequerida);
    this.cantidadRequerida = cantidadRequerida;
  }

  @Override
  public Boolean estaSatisfecha() {
    return getCantidadRecibida() >= cantidadRequerida;
  }

  @Override
  protected Integer getCantidad() {
    return this.cantidadRequerida;
  }

  @Override
  public String getTipo() {
    return "EXTRAORDINARIA";
  }
}
