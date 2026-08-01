package ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.EstadoEntrega;
import java.time.LocalDateTime;

public record EstadoEntregaDto(
    String estado,
    LocalDateTime fecha,
    String detalle,
    String patenteCamion
) { }