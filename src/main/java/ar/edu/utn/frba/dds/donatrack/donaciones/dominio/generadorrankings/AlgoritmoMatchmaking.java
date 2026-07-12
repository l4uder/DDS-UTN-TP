package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class AlgoritmoMatchmaking {

  public List<Beneficiario> generarRanking(Donacion donacion, List<Beneficiario> beneficiarios) {
    Map<Beneficiario, Integer> puntajes = mapearPuntaje(donacion, beneficiarios);

    List<Beneficiario> top10 = puntajes.entrySet().stream()
        .sorted(modoOrdenamiento())
        .limit(10)
        .map(Map.Entry::getKey)
        .toList();

    return top10;
  }

  protected abstract Map<Beneficiario, Integer> mapearPuntaje(Donacion donacion,
                                                              List<Beneficiario> beneficiarios);

  protected abstract Comparator<Map.Entry<Beneficiario, Integer>> modoOrdenamiento();
}
