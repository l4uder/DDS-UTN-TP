package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador.algoritmos;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador.AlgoritmoMatchmaking;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompatibilidadSemantica extends AlgoritmoMatchmaking {

  @Override
  public Map<Beneficiario, Integer> mapearPuntaje(Donacion donacion,
                                                  List<Beneficiario> beneficiarios) {
    return beneficiarios.stream()
        .map(b -> Map.entry(b, calcularnecesidadesCubiertas(b, donacion)))
        .filter(entry -> entry.getValue() >= 1)
        //Solo guardamos aquellos beneficiarios a quien les sirve la donacion
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private Integer calcularnecesidadesCubiertas(Beneficiario beneficiario, Donacion donacion) {
    List<Necesidad> necesidades = beneficiario.getNecesidades();

    List<Necesidad> necesidadesCubiertas = necesidades.stream()
        .filter(n -> donacion.getBienes()
            .stream().anyMatch(b -> b.getSubcategoria().esIgual(n.getSubcategoria()))).toList();

    return necesidadesCubiertas.size();
  }

  @Override
  protected Comparator<Map.Entry<Beneficiario, Integer>> modoOrdenamiento() {
    // ordena de mayor a menor
    return Map.Entry.<Beneficiario, Integer>comparingByValue().reversed();
  }
}