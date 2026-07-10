package ar.edu.utn.frba.dds.donatrack.logistica.dto.entrega;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.EstadoEntrega;

public class EntregaMapper {
  public static EntregaResponse aResponse(Entrega entrega) {
    return new EntregaResponse(
        entrega.getId(),
        entrega.getDestino().getRazonSocial(),
        entrega.getDestino().getDireccion(),
        entrega.getEstadoActual().name(),
        entrega.getCamionAsignado() != null
            ? entrega.getCamionAsignado().getPatente()
            : null,
        entrega.getFotosRecepcion(),
        entrega.getHistorialEstados().stream()
            .map(EntregaMapper::aEstadoDto)
            .toList()
    );
  }

  private static EstadoEntregaDto aEstadoDto(EstadoEntrega estado) {
    return new EstadoEntregaDto(
        estado.getTipoEstado().name(),
        estado.getFecha(),
        estado.getDetalle(),
        estado.getCamion() != null ? estado.getCamion().getPatente() : null
    );
  }
}