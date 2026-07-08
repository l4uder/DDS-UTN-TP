package ar.edu.utn.frba.dds.donatrack.logistica.dto.camion;

public record CamionRequest(
    String patente,
    Float capacidadVolumen,
    Float altura,
    Float capacidadCarga
) {
}
