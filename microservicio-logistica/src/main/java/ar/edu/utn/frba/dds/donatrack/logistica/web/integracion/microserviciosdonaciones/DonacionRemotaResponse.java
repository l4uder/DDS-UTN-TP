package ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.microserviciosdonaciones;

record DonacionRemotaResponse(
    Long id,
    String descripcion,
    BeneficiarioRemotoResponse beneficiario
) {

  record BeneficiarioRemotoResponse(
      Long id,
      String razonSocial,
      String direccion
  ) {
  }
}
