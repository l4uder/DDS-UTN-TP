package ar.edu.utn.frba.dds.donatrack.clasificacion;

import ar.edu.utn.frba.dds.donatrack.donacion.Bien;
import ar.edu.utn.frba.dds.donatrack.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donacion.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.Perecedero;
import ar.edu.utn.frba.dds.donatrack.donante.RegistroEntrega;
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
