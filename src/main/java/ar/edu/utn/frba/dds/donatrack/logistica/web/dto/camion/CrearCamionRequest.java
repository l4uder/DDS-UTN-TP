package ar.edu.utn.frba.dds.donatrack.logistica.web.dto.camion;

public record CrearCamionRequest(
    String patente,
    Float capacidadVolumen,
    Float altura,
    Float capacidadCarga
) {
}