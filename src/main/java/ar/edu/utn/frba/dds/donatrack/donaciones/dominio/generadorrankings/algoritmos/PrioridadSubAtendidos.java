package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.algoritmos;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrioridadSubAtendidos extends AlgoritmoMatchmaking {

  @Override
  public Map<Beneficiario, Integer> mapearPuntaje(Donacion donacion,
                                                  List<Beneficiario> beneficiarios) {
    Map<Beneficiario, Integer> puntajes = new HashMap<>();
    beneficiarios.forEach(b -> puntajes.put(b, calcularDonacionesUltimoTrimestre(b)));
    return puntajes;
  }

  private Integer calcularDonacionesUltimoTrimestre(Beneficiario beneficiario) {
    LocalDateTime haceTresMeses = LocalDateTime.now().minusMonths(3);

    List<Donacion> donacionesUltimos3Meces = beneficiario.getDonaciones().stream()
        .filter(d -> d.getFechaAsignacion().isAfter(haceTresMeses)).toList();

    return donacionesUltimos3Meces.size();
  }

  @Override
  protected Comparator<Map.Entry<Beneficiario, Integer>> modoOrdenamiento() {
    // ordena de menor a mayor
    return Map.Entry.<Beneficiario, Integer>comparingByValue()
        .thenComparing(entry -> entry.getKey().getDonaciones().size());
    //para el desempate, beneficiando a los que no recibieron donaciones
  }
}
