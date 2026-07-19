package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad;

public record NecesidadResponse(
    String id,
    String tipo,
    String descripcion,
    String categoria,
    String subcategoria,
    Integer cantidadRecibida,
    Boolean satisfecha,
    Integer cantidadPorPeriodo,
    String periodo,
    Integer cantidadRequerida
) {
}
