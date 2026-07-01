package ar.edu.utn.frba.dds.donatrack.dominio.asignador;

import ar.edu.utn.frba.dds.donatrack.dominio.asignador.algoritmos.AlgoritmoMatchmaking;
import ar.edu.utn.frba.dds.donatrack.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.dominio.donacion.Donacion;
import java.util.ArrayList;
import java.util.List;

public class Asignador {
  private List<AlgoritmoMatchmaking> algoritmosMatch;

  public Asignador() {
    this.algoritmosMatch = new ArrayList<>();
  }

  public void agregarAlgoritmo(AlgoritmoMatchmaking algoritmo) {
    this.algoritmosMatch.add(algoritmo);
  }

  public List<ResultadoAsignacion> asignar(List<Donacion> donaciones,
                                           List<Beneficiario> beneficiarios) {
    return donaciones.stream()
        .map(d -> new ResultadoAsignacion(d, asignar(d, beneficiarios))).toList();
  }

  public List<Beneficiario> asignar(Donacion donacion, List<Beneficiario> beneficiarios) {
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
