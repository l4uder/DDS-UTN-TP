package ar.edu.utn.frba.dds.donatrack.logistica.dto.ruta;

import java.time.LocalDate;

public record RutaRequest(
    String patenteCamion,
    LocalDate fecha
) {}
