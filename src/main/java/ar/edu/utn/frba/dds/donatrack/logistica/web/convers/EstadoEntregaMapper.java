package ar.edu.utn.frba.dds.donatrack.logistica.web.convers;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.EstadoEntrega;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega.EstadoEntregaDto;
import java.util.List;

public class EstadoEntregaMapper {

  public static EstadoEntregaDto aDto(EstadoEntrega estado) {
    return new EstadoEntregaDto(
        estado.getTipoEstado().name(),
        estado.getFecha(),
        estado.getDetalle(),
        estado.getCamion()!=null ? estado.getCamion().getPatente() : null
    );
  }

  public static List<EstadoEntregaDto> aDto(List<EstadoEntrega> estados) {
    return estados.stream().map(EstadoEntregaMapper::aDto).toList();
  }

}
