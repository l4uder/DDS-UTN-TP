package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion;

import java.time.LocalDateTime;
import java.util.List;

public record RankingResponse(
    String donacionId,
    LocalDateTime fechaGeneracion,
    List<CandidatoDto> candidatos
) {
}
