package ar.edu.utn.frba.dds.donatrack.logistica.dto.camion;

public record CamionResponse(
    String patente,
    float capacidadVolumen,
    float altura,
    float capacidadCarga
) {
}
