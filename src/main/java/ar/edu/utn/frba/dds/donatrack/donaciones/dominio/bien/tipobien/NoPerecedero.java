package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import lombok.Getter;

@Getter
public class NoPerecedero implements TipoBien {
  private Boolean estaUsado;

  public NoPerecedero(Boolean usado) {
    if (usado == null) throw new DominioException("El campo 'usado' es obligatorio, en el Bien No Perecedero");
    this.estaUsado = usado;
  }

  @Override
  public String getNombreClave(Subcategoria subcategoria) {
    return subcategoria.getNombre() + "_" + (estaUsado ? "usado" : "nuevo");
  }

}