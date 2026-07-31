package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.bien.BienDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.DonacionRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.DonacionResponse;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.DonacionResumenResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DonacionMapper {

  public static Donacion aDominio(List<BienDto> bienesDto, List<Donante> donantes) {
      return new Donacion(BienMapper.aDominio(bienesDto), donantes);
  }

  public static DonacionResponse aDto(Donacion donacion) {
    return new DonacionResponse(
        donacion.getId(),
        donacion.getDescripcion(),
        donacion.getEstadoActual().name(),
        donacion.getBeneficiario() == null ? null : BeneficiarioMapper.aDtoResumen(donacion.getBeneficiario()),
        BienMapper.aDto(donacion.getBienes()));
  }

  public static DonacionResumenResponse aDtoResumen(Donacion donacion) {
    return new DonacionResumenResponse(
        donacion.getId(),
        donacion.getDescripcion(),
        donacion.getEstadoActual().name(),
        donacion.getBeneficiario() == null ? null : BeneficiarioMapper.aDtoResumen(donacion.getBeneficiario())
    );
  }

  public static List<DonacionResumenResponse> aDto(List<Donacion> donaciones) {
    return donaciones.stream().map(DonacionMapper::aDtoResumen).toList();
  }

}
