package ar.edu.utn.frba.dds.donatrack.donacion;

public enum UnidadMedida {
  KILOGRAMOS("kg"),
  UNIDADES("unidades"),
  LITROS("l"),
  GRAMOS("g");

  private final String label;

  UnidadMedida(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return this.label;
  }
}