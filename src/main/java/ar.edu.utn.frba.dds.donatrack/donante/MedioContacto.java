package ar.edu.utn.frba.dds.donatrack.donante;

public class MedioContacto {
  private tipoContacto tipo;
  private String detalle;

  public MedioContacto(tipoContacto tipo, String detallee) {
      this.tipo = tipo;
      this.detalle = detallee;
  }

  public tipoContacto getTipo() {
    return tipo;
  }

  public String getDetalle() {
     return detalle;
  }
}
