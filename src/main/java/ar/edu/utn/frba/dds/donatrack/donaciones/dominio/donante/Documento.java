package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import lombok.Getter;

@Getter
public class Documento {
  private TipoDocumento tipoDocumento;
  private String detalle;

  public Documento(TipoDocumento tipoDocumento, String detalle) {
    this.tipoDocumento = tipoDocumento;
    this.detalle = detalle;
  }

  public Boolean esIgualA(Documento otrodocumento) {
    return this.tipoDocumento == otrodocumento.getTipoDocumento()
            && this.detalle.equalsIgnoreCase(otrodocumento.getDetalle());
  }

}
