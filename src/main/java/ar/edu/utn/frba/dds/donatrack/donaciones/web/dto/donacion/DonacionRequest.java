package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion;

import java.util.List;

public record DonacionRequest(
    List<BienDto> bienes,
    List<String> donanteIds
) {
}

