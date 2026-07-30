package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoPersona;
import java.util.Arrays;
import java.util.List;

public enum TipoDocumento {
  DNI(TipoPersona.HUMANA),
  CUIT(TipoPersona.JURIDICA), // Es posible hacer: CUIT(TipoDonante.JURIDICA, TipoDonante.HUMANA)
  PASAPORTE(TipoPersona.HUMANA);

  private final List<TipoPersona> tiposPermitidos;

  TipoDocumento(TipoPersona... permitidos) {
    this.tiposPermitidos = Arrays.asList(permitidos);
  }

  public static List<TipoDocumento> values(TipoPersona tipoDonante) {
    return Arrays.stream(values())
        .filter(td -> td.tiposPermitidos.contains(tipoDonante))
        .toList();
  }

}