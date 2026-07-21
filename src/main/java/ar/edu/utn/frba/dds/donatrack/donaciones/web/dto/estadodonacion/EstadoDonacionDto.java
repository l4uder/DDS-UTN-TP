package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.estadodonacion;

import java.time.LocalDateTime;

public record EstadoDonacionDto(String estado, LocalDateTime fecha, String detalle) {
}
