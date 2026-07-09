package ar.edu.utn.frba.dds.donatrack.logistica.integracion;

/**
 * Shape mínimo del JSON devuelto por GET /donaciones del servicio de donaciones.
 * Gson ignora campos extra (estado, bienes, contactos, etc.).
 */
record DonacionRemotaResponse(
    String id,
    String descripcion,
    BeneficiarioRemotoResponse beneficiario
) {

  record BeneficiarioRemotoResponse(
      String id,
      String razonSocial,
      String direccion
  ) {
  }
}
