package ar.edu.utn.frba.dds.donatrack.logistica.dto.ruta;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Ruta;

public class RutaMapper {
  public static RutaResponse aResponse(Ruta ruta) {
    return new RutaResponse(
        ruta.getId(),
        ruta.getCamion().getPatente(),
        ruta.getChofer() != null
            ? ruta.getChofer().getNombre() + " " + ruta.getChofer().getApellido()
            : null,
        ruta.getFecha(),
        ruta.isIniciada(),
        ruta.getEntregasOrdenadas().stream()
            .map(e -> e.getId())
            .toList()
    );
  }
}