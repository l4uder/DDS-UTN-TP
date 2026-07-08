package ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion;

import java.util.List;

public record DonacionRequest(
    List<BienDto> bienes
) {
}
