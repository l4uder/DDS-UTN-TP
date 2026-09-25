package ar.edu.utn.frba.dds.donatrack.logistica.web.dto.ruta;

import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega.EntregaResumenResponse;
import java.time.LocalDate;
import java.util.List;

public record RutaResponse(
    String id,
    String patenteCamion,
    String nombreChofer,
    LocalDate fecha,
    boolean iniciada,
    List<EntregaResumenResponse> entregas
) { }