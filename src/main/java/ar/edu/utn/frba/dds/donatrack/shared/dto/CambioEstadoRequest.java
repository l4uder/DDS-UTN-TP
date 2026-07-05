package ar.edu.utn.frba.dds.donatrack.shared.dto;

/**
 * Contrato entre servicios: logística notifica cambios de estado de una donación
 * vía POST /donaciones/{id}/estados con este body.
 */
public record CambioEstadoRequest(String estado, String observacion) {
}
