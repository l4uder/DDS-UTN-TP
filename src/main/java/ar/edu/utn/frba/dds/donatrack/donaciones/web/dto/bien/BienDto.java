package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.bien;

import java.time.LocalDate;

public record BienDto(
    String tipo,
    String descripcion,
    Float cantidad,
    String unidadMedida,
    String foto,
    String categoria,
    String subcategoria,
    LocalDate fechaVencimiento,
    Boolean usado
) {
}
