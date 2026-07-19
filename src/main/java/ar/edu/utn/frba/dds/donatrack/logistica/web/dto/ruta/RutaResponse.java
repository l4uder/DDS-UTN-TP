package ar.edu.utn.frba.dds.donatrack.logistica.web.dto.ruta;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Ruta;
import java.time.LocalDate;
import java.util.List;

public record RutaResponse(
    String id,
    String patenteCamion,
    String nombreChofer,
    LocalDate fecha,
    boolean iniciada,
    List<String> idsEntregas
) {

  public static RutaResponse desde(Ruta ruta) {

    return new RutaResponse(
        ruta.getId(),
        ruta.getCamion().getPatente(),
        ruta.getChofer() != null
            ? ruta.getChofer().getNombre()
              + " "
              + ruta.getChofer().getApellido()
            : null,
        ruta.getFecha(),
        ruta.isIniciada(),
        ruta.getEntregasOrdenadas()
            .stream()
            .map(Entrega::getId)
            .toList()
    );
  }
}