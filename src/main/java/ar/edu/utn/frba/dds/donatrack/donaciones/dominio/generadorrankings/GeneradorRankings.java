package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.RankingRepository;
import java.util.ArrayList;
import java.util.List;

public class GeneradorRankings {
  private List<AlgoritmoMatchmaking> algoritmosMatch;
  private RankingRepository repoRankings;

  public GeneradorRankings(RankingRepository repoRankings) {
    this.algoritmosMatch = new ArrayList<>();
    this.repoRankings = repoRankings;
  }

  public void agregarAlgoritmo(AlgoritmoMatchmaking algoritmo) {
    this.algoritmosMatch.add(algoritmo);
  }

  public List<Ranking> asignar(List<Donacion> donaciones, List<Beneficiario> beneficiarios) {
    List<Ranking> rankings = donaciones.stream()
        .map(d -> new Ranking(d, asignar(d, beneficiarios)))
        .toList();

    rankings.forEach(r -> repoRankings.guardar(r));

    return rankings;
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
