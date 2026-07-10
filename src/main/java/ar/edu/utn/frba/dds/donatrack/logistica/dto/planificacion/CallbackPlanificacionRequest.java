package ar.edu.utn.frba.dds.donatrack.logistica.dto.planificacion;

import java.util.List;
import java.util.Map;

public record CallbackPlanificacionRequest(
    Map<String, List<String>> entregasPorPatente,
    List<String> entregasSinAsignar,
    String fecha
) {}
