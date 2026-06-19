package ar.edu.utn.frba.dds.donatrack.donacion;

public class NoPerecedero implements TipoBien {
  private Boolean usado;

  public NoPerecedero(Boolean usado) {
    this.usado = usado;
  }

  public Boolean esUsado() {
    return this.usado;
  }

  @Override
  public String getNombreClave(Subcategoria subcategoria) {
    return subcategoria.getNombre() + "_" + (usado ? "usado" : "nuevo");
  }
}