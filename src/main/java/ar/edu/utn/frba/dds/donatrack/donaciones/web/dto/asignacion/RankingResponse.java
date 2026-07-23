package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.DonacionResponse;
import java.time.LocalDateTime;
import java.util.List;

public record RankingResponse(
    String id,
    LocalDateTime fechaGeneracion,
    DonacionResponse donacion,
    List<CandidatoDto> candidatos
) { }
