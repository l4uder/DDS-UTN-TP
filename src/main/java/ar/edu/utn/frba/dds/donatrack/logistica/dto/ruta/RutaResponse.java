package ar.edu.utn.frba.dds.donatrack.logistica.dto.ruta;

import java.time.LocalDate;
import java.util.List;

public record RutaResponse(
    String id,
    String patenteCamion,
    String nombreChofer,
    LocalDate fecha,
    boolean iniciada,
    List<String> idsEntregas
) {}
