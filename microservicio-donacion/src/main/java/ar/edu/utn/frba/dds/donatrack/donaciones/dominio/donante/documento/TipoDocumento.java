package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento;

import java.util.List;

public enum TipoDocumento {
  DNI,
  CUIT,
  PASAPORTE;

  public static List<TipoDocumento> valoresPosiblesHumana() {
    return List.of(DNI, PASAPORTE);
  }

  public static List<TipoDocumento> valoresPosiblesJuridica() {
    return List.of(CUIT);
  }

}