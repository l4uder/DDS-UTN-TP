package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad;

public record NecesidadRequest(
    String tipo,
    String descripcion,
    String unidadMedida,
    String categoria,
    String subcategoria,
    Integer cantidadPorPeriodo,
    String periodo,
    Integer cantidadRequerida
) {
}
