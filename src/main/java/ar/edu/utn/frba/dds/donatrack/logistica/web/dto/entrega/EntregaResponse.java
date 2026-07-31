package ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import java.util.List;

public record EntregaResponse(
    String id,
    String destinoRazonSocial,
    String destinoDireccion,
    String estadoActual,
    String patenteCamion,
    List<String> fotos,
    List<EstadoEntregaDto> historial
) { }