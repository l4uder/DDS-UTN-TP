package ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega;

public record EntregaResumenResponse(
    Long id,
    String destinoRazonSocial,
    String destinoDireccion,
    String estadoActual
) { }
