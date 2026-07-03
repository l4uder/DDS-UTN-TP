package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;

public enum UnidadMedida {
  KILOGRAMOS("kg"),
  LITROS("lt"),
  GRAMOS("gr"),
  SIN_UNIDAD("sin medida");

  private final String label;

  UnidadMedida(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return this.label;
  }
}