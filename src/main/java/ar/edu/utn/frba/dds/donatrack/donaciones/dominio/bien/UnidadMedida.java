package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;

public enum UnidadMedida {
  //medidas de masa
  KILOGRAMOS("kg", 1000),
  GRAMOS("gr", 1),
  //medidas de capacidad
  LITROS("lt", 1000),
  MILILITROS("ml", 1),
  //valores por unidad
  UNIDADES("unidad", 1);

  private final String abreviatura;
  private final Integer factorConversor;

  UnidadMedida(String label, Integer numero) {
    this.abreviatura = label;
    this.factorConversor = numero;
  }

  public float convertirAMenorMedida(float cantidad) {
    return cantidad * factorConversor;
  }

}