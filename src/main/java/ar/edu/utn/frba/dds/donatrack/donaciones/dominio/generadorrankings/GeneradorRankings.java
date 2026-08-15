package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.algoritmos.AlgoritmoMatchmaking;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.RankingRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.ArrayList;
import java.util.List;

public class GeneradorRankings {
  private final RankingRepository repoRankings;
  private final List<AlgoritmoMatchmaking> algoritmosMatch;

  public GeneradorRankings(RankingRepository repoRankings) {
    this.repoRankings = repoRankings;
    this.algoritmosMatch = new ArrayList<>();
  }

  public void agregarAlgoritmo(AlgoritmoMatchmaking algoritmo) {
    this.algoritmosMatch.add(algoritmo);
  }

  public List<Ranking> generar(List<Donacion> donaciones, List<Beneficiario> beneficiarios) {
    if (beneficiarios == null || beneficiarios.isEmpty()) throw new DominioException("No se puede generar rankings sino hay beneficiarios");

    List<Ranking> rankings = donaciones.stream()
        .map(d -> generarRanking(d, beneficiarios)).toList();

    rankings.forEach(r -> repoRankings.guardar(r));

    return rankings;
  }

  //========================= FUNCIONES AUXILIARES ===========================
  private Ranking generarRanking(Donacion donacion, List<Beneficiario> beneficiarios) {
    List<List<Beneficiario>> beneficiariosPorAlgoritmo = algoritmosMatch.stream()
        .map(a -> a.elegirCandidatos(donacion, beneficiarios))
        .toList();

    List<Beneficiario> todosLosPosiblesBeneficiarios = beneficiariosPorAlgoritmo.stream()
        .flatMap(l -> l.stream())
        .distinct().toList();

    List<Beneficiario> beneficiariosCoincidentes = todosLosPosiblesBeneficiarios.stream()
            .filter(beneficiario -> beneficiariosPorAlgoritmo.stream()
                .allMatch(a -> a.stream()
                    .anyMatch(b -> b.esIgual(beneficiario)))).toList();

    if (beneficiariosCoincidentes.isEmpty()) {
      System.out.println("nose encontró coincidencias entre los algoritmos se devuelve todo");
      return new Ranking(donacion, todosLosPosiblesBeneficiarios);
    }

    return new Ranking(donacion, beneficiariosCoincidentes);
  }

}
