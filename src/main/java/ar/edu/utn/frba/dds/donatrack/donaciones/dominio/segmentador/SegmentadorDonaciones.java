package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.segmentador;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.RegistroEntrega;
import java.util.List;
import java.util.stream.Collectors;

public class SegmentadorDonaciones {

  public List<Donacion> segmentar(List<RegistroEntrega> registros) {
    return registros.stream()
        .flatMap(registro -> registro.getBienes().stream())
        .collect(Collectors.groupingBy(Bien::getNombreClave))
        .values().stream()
        .map(Donacion::new)
        .toList();
  }
}
