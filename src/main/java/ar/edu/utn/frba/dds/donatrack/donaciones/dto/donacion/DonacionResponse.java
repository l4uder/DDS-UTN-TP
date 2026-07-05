package ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion;

import java.util.List;

public record DonacionResponse(
    String id,
    String descripcion,
    String estado,
    String beneficiario,
    List<BienDto> bienes
) {
}
