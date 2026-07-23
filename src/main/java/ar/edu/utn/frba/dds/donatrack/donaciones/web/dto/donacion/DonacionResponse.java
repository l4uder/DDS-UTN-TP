package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioResumenResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.bien.BienDto;
import java.util.List;

public record DonacionResponse(
    String id,
    String descripcion,
    String estado,
    BeneficiarioResumenResponse beneficiario,
    List<BienDto> bienes
) { }
