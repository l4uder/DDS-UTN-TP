package ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;

import java.util.List;

public record DonacionRequest(
    List<BienDto> bienes,
    List<String> donanteIds
) {
}
