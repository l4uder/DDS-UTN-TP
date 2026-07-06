package ar.edu.utn.frba.dds.donatrack.donaciones.dto.beneficiario;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.comun.ContactoMapper;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;

public class BeneficiarioMapper {

  private BeneficiarioMapper() {
  }

  public static Beneficiario aDominio(BeneficiarioRequest request) {
    validar(request);
    return new Beneficiario(
        request.razonSocial(),
        request.direccion(),
        ContactoMapper.aDominio(request.contactos()));
  }

  public static void validar(BeneficiarioRequest request) {
    if (request.razonSocial() == null || request.razonSocial().isBlank()) {
      throw new DomainValidationException("El campo 'razonSocial' es obligatorio");
    }
    if (request.direccion() == null || request.direccion().isBlank()) {
      throw new DomainValidationException("El campo 'direccion' es obligatorio");
    }
  }

  public static BeneficiarioResponse aResponse(Beneficiario beneficiario) {
    return new BeneficiarioResponse(
        beneficiario.getId(),
        beneficiario.getRazonSocial(),
        beneficiario.getDireccion(),
        beneficiario.getContactos().stream().map(ContactoMapper::aDto).toList());
  }

}
