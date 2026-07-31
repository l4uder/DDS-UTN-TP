package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.candidato;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioResumenResponse;

public record CandidatoDto(
    int posicion,
    BeneficiarioResumenResponse beneficiario
) { }
