package ar.edu.utn.frba.dds.donatrack.dominio.bien.tipobien;

import ar.edu.utn.frba.dds.donatrack.dominio.bien.Subcategoria;

public class NoPerecedero implements TipoBien {
  private Boolean estaUsado;

  public NoPerecedero(Boolean usado) {
    this.estaUsado = usado;
  }

  public Boolean getEstaUsado() {
    return this.estaUsado;
  }

  @Override
  public String getNombreClave(Subcategoria subcategoria) {
    return subcategoria.getNombre() + "_" + (estaUsado ? "usado" : "nuevo");
  }
}