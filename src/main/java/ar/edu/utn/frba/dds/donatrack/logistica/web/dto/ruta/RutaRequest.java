package ar.edu.utn.frba.dds.donatrack.logistica.web.dto.ruta;

import java.time.LocalDate;

public record RutaRequest(
    String patenteCamion,
    LocalDate fecha
) {}
