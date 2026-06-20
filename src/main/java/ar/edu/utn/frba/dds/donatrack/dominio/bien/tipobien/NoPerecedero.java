package ar.edu.utn.frba.dds.donatrack.dominio.bien.tipobien;

import ar.edu.utn.frba.dds.donatrack.dominio.bien.Subcategoria;

public class NoPerecedero implements TipoBien {
  private Boolean usado;

  public NoPerecedero(Boolean usado) {
    this.usado = usado;
  }

  public Boolean getUsado() {
    return this.usado;
  }

  @Override
  public String getNombreClave(Subcategoria subcategoria) {
    return subcategoria.getNombre() + "_" + (usado ? "usado" : "nuevo");
  }
}