package ar.edu.utn.frba.dds.donatrack.share;

import ar.edu.utn.frba.dds.donatrack.exception.DomainValidationException;

public class MedioContacto {
  private TipoContacto tipo;
  private String detalle;

  public MedioContacto(TipoContacto tipo, String detallee) {
    this.tipo = tipo;
    if (detallee == null || detallee.isBlank()) {
       throw new DomainValidationException("contacto sin dato, falta el detalle");
    }
    this.detalle = detallee;
  }

  public TipoContacto getTipo() {
    return tipo;
  }

  public String getDetalle() {
    return detalle;
  }
}
