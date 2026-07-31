package ar.edu.utn.frba.dds.donatrack.logistica.web.convers;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega.EntregaResponse;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega.EntregaResumenResponse;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega.EstadoEntregaDto;
import java.util.List;

public class EntregaMapper {

  public static EntregaResponse aDto(Entrega entrega) {
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

  public static EntregaResumenResponse aDtoResumen(Entrega entrega) {
    return new EntregaResumenResponse(
        entrega.getId(),
        entrega.getDestino().getRazonSocial(),
        entrega.getDestino().getDireccion(),
        entrega.getEstadoActual().name()
    );
  }

  public static List<EntregaResumenResponse> aDtoResumen(List<Entrega> entregas) {
    return entregas.stream().map(EntregaMapper::aDtoResumen).toList();
  }
}
