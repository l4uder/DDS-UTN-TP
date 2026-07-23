package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad;

import lombok.Builder;

@Builder
public record NecesidadResponse(
    String id,
    String tipo,
    String descripcion,
    String unidadMedida,
    String categoria,
    String subcategoria,
    Integer cantidadRecibida,
    Boolean estaSatisfecha,
    Integer cantidadPorPeriodo,
    String periodo,
    Integer cantidadRequerida
) { }
