package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion;

import java.time.LocalDateTime;

public record EstadoDonacionDto(String estado, LocalDateTime fecha, String detalle) {
}
