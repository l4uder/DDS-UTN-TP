package ar.edu.utn.frba.dds.donatrack.logistica.dto.entrega;

import java.time.LocalDateTime;

public record EstadoEntregaDto(
    String estado,
    LocalDateTime fecha,
    String detalle,
    String patenteCamion
) {}