package ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.dto.beneficiario.BeneficiarioResponse;
import java.util.List;

public record DonacionResponse(
    String id,
    String descripcion,
    String estado,
    BeneficiarioResponse beneficiario,
    List<BienDto> bienes
) {
}
