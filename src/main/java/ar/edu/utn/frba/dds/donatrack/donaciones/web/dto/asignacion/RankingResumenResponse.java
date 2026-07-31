package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion;

import java.time.LocalDateTime;

public record RankingResumenResponse (
    String id,
    LocalDateTime fechaGeneracion
) { }
