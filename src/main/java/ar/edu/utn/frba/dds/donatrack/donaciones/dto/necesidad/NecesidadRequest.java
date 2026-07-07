package ar.edu.utn.frba.dds.donatrack.donaciones.dto.necesidad;

public record NecesidadRequest(
    String tipo,
    String descripcion,
    String categoria,
    String subcategoria,
    Integer cantidadPorPeriodo,
    String periodo,
    Integer cantidadRequerida
) {
}
