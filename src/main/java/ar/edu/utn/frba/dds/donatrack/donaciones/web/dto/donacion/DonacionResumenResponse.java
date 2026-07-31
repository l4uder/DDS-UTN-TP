package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioResumenResponse;

public record DonacionResumenResponse(
    String id,
    String descripcion,
    String estado,
    BeneficiarioResumenResponse beneficiario
) { }
