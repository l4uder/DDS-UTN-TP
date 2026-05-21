package ar.edu.utn.frba.dds.donatrack.donante;

public class MedioContacto {
  private TipoContacto tipo;
  private String detalle;

  public MedioContacto(TipoContacto tipo, String detallee) {
      this.tipo = tipo;
      this.detalle = detallee;
  }

  public TipoContacto getTipo() {
    return tipo;
  }

  public String getDetalle() {
     return detalle;
  }
}
