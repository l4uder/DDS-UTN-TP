package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public class NecesidadRecurrente extends Necesidad {
  private Integer cantidadPorPeriodo;
  private Periodo periodo;

  public NecesidadRecurrente(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, int cantidadPorPeriodo, Periodo periodo) {
    super(subcategoria, unidadMedida, descripcion);
    checkDatos(cantidadPorPeriodo, periodo);
    this.cantidadPorPeriodo = cantidadPorPeriodo;
    this.periodo = periodo;
  }

  private void checkDatos(Integer cantidadPorPeriodo, Periodo periodo) {
    if (cantidadPorPeriodo == null || cantidadPorPeriodo <= 0) {
      throw new DominioException( "Una necesidad recurrente necesita 'cantidadPorPeriodo' mayor a cero");
    }
    if (periodo == null) {
      throw new DominioException( "Una necesidad recurrente necesita 'periodo' puede ser: " + Arrays.toString(Periodo.values()));
    }
  }

  public void actualizarDatos(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, int cantidadPorPeriodo, Periodo periodo) {
    super.actualizarDatosBase(subcategoria, unidadMedida, descripcion);
    checkDatos(cantidadPorPeriodo, periodo);
    this.cantidadPorPeriodo = cantidadPorPeriodo;
    this.periodo = periodo;
  }

  @Override
  public Boolean estaSatisfecha() {
    return getCantidadRecibida() >= cantidadPorPeriodo;
  }

  @Override
  protected Integer getCantidadQueNecesita() {
    return this.cantidadPorPeriodo;
  }

  @Override
  public String getTipo() {
    return "RECURRENTE";
  }
}

