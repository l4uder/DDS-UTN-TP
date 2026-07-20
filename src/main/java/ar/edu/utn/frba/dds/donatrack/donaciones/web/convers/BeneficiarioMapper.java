package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioResumenDto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;

public class BeneficiarioMapper {

  private BeneficiarioMapper() {}

  public static Beneficiario aDominio(BeneficiarioRequest request) {
    return new Beneficiario(
        request.razonSocial(),
        request.direccion(),
        ContactoMapper.aDominio(request.contactos()));
  }

  public static BeneficiarioResponse aDto(Beneficiario beneficiario) {
    return new BeneficiarioResponse(
        beneficiario.getId(),
        beneficiario.getRazonSocial(),
        beneficiario.getDireccion(),
        ContactoMapper.aDto(beneficiario.getContactos()));
  }

  public static BeneficiarioResumenDto aDtoResumen(Beneficiario beneficiario) {
    return new BeneficiarioResumenDto(
        beneficiario.getId(),
        beneficiario.getRazonSocial(),
        beneficiario.getDireccion());
  }

}
