package ar.edu.utn.frba.dds.donatrack.donante;

public class Documento {
  private TipoDocumento tipoDocumento;
  private String detalle;

  public Documento(TipoDocumento tipoDocumento, String detalle) {
    this.tipoDocumento = tipoDocumento;
    this.detalle = detalle;
  }

  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }

  public String getDetalle() {
      return detalle;
  }


  public Boolean esIgualA(Documento otrodocumento) {
      return this.tipoDocumento == otrodocumento.getTipoDocumento() &&
              this.detalle.equalsIgnoreCase(otrodocumento.getDetalle());
  }
}
