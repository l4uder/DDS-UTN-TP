package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioResumenDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.bien.BienDto;
import java.util.List;

public record DonacionResponse(
    String id,
    String descripcion,
    String estado,
    BeneficiarioResumenDto beneficiario,
    List<BienDto> bienes
) {
}
