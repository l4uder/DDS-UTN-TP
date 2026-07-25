package ar.edu.utn.frba.dds.donatrack.logistica.web.dto.camion;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;

public record CamionResponse(
    String patente,
    float capacidadVolumen,
    float altura,
    float capacidadCarga
) { }