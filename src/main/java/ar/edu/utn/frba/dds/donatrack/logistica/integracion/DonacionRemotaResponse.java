package ar.edu.utn.frba.dds.donatrack.logistica.integracion;

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
