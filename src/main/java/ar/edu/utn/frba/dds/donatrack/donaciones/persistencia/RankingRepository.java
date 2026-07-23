package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.Ranking;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class RankingRepository {
  private static final RankingRepository INSTANCE = new RankingRepository();
  private final Map<String, Ranking> storeRankings;

  private RankingRepository() {
    storeRankings = new HashMap<>();
  }

  public static RankingRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Ranking ranking) {
    if (ranking.getId() != null) throw new IllegalArgumentException("Constraint Violations: " + "El ranking ya tiene un ID asignado: " + ranking.getId());
    ranking.setId(UUID.randomUUID().toString());

    this.storeRankings.put(ranking.getId(), ranking);
  }

  public Ranking buscarPorId(String id) {
    return storeRankings.get(id);
  }

  public List<Ranking> buscarTodos() {
    return storeRankings.values().stream().filter(Ranking::getEsVigente).toList();
  }

  public void actualizar(Ranking ranking) {
    if (ranking.getId() == null || !this.storeRankings.containsKey(ranking.getId())) {
      throw new IllegalArgumentException("La donación No existe en la base de dato");
    }
    this.storeRankings.put(ranking.getId(), ranking);
  }

  public void vaciarSoft() {
    buscarTodos().forEach(Ranking::vencida);
  }

}
