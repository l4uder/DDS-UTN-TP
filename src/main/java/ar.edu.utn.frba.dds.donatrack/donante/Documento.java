package ar.edu.utn.frba.dds.donatrack.donante;

public class Documento {
  private TipoDocumento tipoDocumento;
  private String detalle;

  public Documento(TipoDocumento tipoDocumento, String detalle) {
    this.tipoDocumento = tipoDocumento;
    this.detalle = detalle;
  }
}
