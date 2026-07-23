package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioResumenResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeneficiarioMapper {

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

  public static BeneficiarioResumenResponse aDtoResumen(Beneficiario beneficiario) {
    return new BeneficiarioResumenResponse(
        beneficiario.getId(),
        beneficiario.getRazonSocial(),
        beneficiario.getDireccion());
  }

  public static void actualizarDominio(Beneficiario beneficiario, BeneficiarioRequest request) {
    String razonSocialMerge = request.razonSocial() != null ? request.razonSocial() : beneficiario.getRazonSocial();
    String direccionMerge = request.direccion() != null ? request.direccion() : beneficiario.getDireccion();
    List<MedioContacto> contactosMerge = request.contactos() != null ? ContactoMapper.aDominio(request.contactos()) : beneficiario.getContactos();

    beneficiario.actualizarDatos(razonSocialMerge, direccionMerge, contactosMerge);
  }

}
