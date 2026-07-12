package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import java.util.ArrayList;
import java.util.List;

public class GeneradorRankings {
  private List<AlgoritmoMatchmaking> algoritmosMatch;

  public GeneradorRankings() {
    this.algoritmosMatch = new ArrayList<>();
  }

  public void agregarAlgoritmo(AlgoritmoMatchmaking algoritmo) {
    this.algoritmosMatch.add(algoritmo);
  }

  public List<Ranking> asignar(List<Donacion> donaciones,
                                           List<Beneficiario> beneficiarios) {
    return donaciones.stream()
        .map(d -> new Ranking(d, asignar(d, beneficiarios))).toList();
  }

  private List<Beneficiario> asignar(Donacion donacion, List<Beneficiario> beneficiarios) {
    List<List<Beneficiario>> rankingsPorAlgoritmo = algoritmosMatch.stream()
        .map(a -> a.generarRanking(donacion, beneficiarios))
        .toList();

    List<Beneficiario> todosLosPosiblesBeneficiarios = rankingsPorAlgoritmo.stream()
        .flatMap(l -> l.stream())
        .distinct().toList();

    List<Beneficiario> beneficiariosElegidos = todosLosPosiblesBeneficiarios.stream()
            .filter(beneficiario -> rankingsPorAlgoritmo.stream()
                .allMatch(a -> a.stream()
                    .anyMatch(b -> b.esIgual(beneficiario)))).toList();

    if (beneficiariosElegidos.isEmpty()) {
      return todosLosPosiblesBeneficiarios;
    }

    return beneficiariosElegidos;
  }
}
