package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.bien.BienDto;
import java.util.List;

public record DonacionRequest(
    List<BienDto> bienes,
    List<String> donantesId
) { }

