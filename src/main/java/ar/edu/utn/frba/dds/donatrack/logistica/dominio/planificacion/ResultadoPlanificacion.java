package ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import java.util.List;
import java.util.Map;

public record ResultadoPlanificacion(
    Map<Camion, List<Entrega>> entregasPorCamion,
    List<Entrega> entregasSinAsignar
) {}