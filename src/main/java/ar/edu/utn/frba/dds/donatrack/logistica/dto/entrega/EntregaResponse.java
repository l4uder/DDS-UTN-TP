package ar.edu.utn.frba.dds.donatrack.logistica.dto.entrega;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import java.util.List;

public record EntregaResponse(
    String id,
    String destinoRazonSocial,
    String destinoDireccion,
    String estadoActual,
    String patenteCamion,
    List<String> fotos,
    List<EstadoEntregaDto> historial
) {


  public static EntregaResponse desde(Entrega entrega){

    return new EntregaResponse(
        entrega.getId(),
        entrega.getDestino().getRazonSocial(),
        entrega.getDestino().getDireccion(),
        entrega.getEstadoActual().name(),
        entrega.getCamionAsignado()!=null
            ? entrega.getCamionAsignado().getPatente()
            : null,
        entrega.getFotosRecepcion(),
        entrega.getHistorialEstados()
            .stream()
            .map(EstadoEntregaDto::desde)
            .toList()
    );
  }

}