package ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.EstadoEntrega;
import java.time.LocalDateTime;

public record EstadoEntregaDto(
    String estado,
    LocalDateTime fecha,
    String detalle,
    String patenteCamion
){

  public static EstadoEntregaDto desde(
      EstadoEntrega estado
  ){

    return new EstadoEntregaDto(
        estado.getTipoEstado().name(),
        estado.getFecha(),
        estado.getDetalle(),
        estado.getCamion()!=null
            ? estado.getCamion().getPatente()
            : null
    );
  }

}