package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.EstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.estadodonacion.EstadoDonacionDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstadoDonacionMapper {

  public static EstadoDonacionDto aDto(EstadoDonacion estado) {
    return new EstadoDonacionDto(
        estado.getTipoEstado().name(),
        estado.getFecha(),
        estado.getDetalle());
  }

  public static List<EstadoDonacionDto> aDto(List<EstadoDonacion> estados) {
    return estados.stream().map(EstadoDonacionMapper::aDto).toList();
  }

}
