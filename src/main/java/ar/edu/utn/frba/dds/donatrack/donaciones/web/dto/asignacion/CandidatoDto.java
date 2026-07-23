package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioResumenResponse;

public record CandidatoDto(
    int posicion,
    BeneficiarioResumenResponse beneficiario
) { }
