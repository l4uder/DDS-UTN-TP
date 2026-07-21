package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.DonacionRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.DonacionResponse;

import java.util.List;

public class DonacionMapper {

  private DonacionMapper() { }

  public static Donacion aDominio(DonacionRequest request, List<Donante> donantes) {
      return new Donacion(BienMapper.aDominio(request.bienes()), donantes);
  }

  public static DonacionResponse aDto(Donacion donacion) {
    return new DonacionResponse(
        donacion.getId(),
        donacion.getDescripcion(),
        donacion.getEstadoActual().name(),
        donacion.getBeneficiario() == null ? null
            : BeneficiarioMapper.aDtoResumen(donacion.getBeneficiario()),
        donacion.getBienes() == null ? null
            : BienMapper.aDto(donacion.getBienes()));
  }

  public static List<DonacionResponse> aDto(List<Donacion> donaciones) {
    return donaciones.stream().map(DonacionMapper::aDto).toList();
  }

}
