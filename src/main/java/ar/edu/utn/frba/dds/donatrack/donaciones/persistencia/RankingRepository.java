package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador.Ranking;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class RankingRepository {
  private static final RankingRepository INSTANCE = new RankingRepository();
  private final Map<String, Ranking> rankingsPorDonacion;

  private RankingRepository() {
    rankingsPorDonacion = new HashMap<>();
  }

  public static RankingRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Ranking ranking) {
    rankingsPorDonacion.put(ranking.getDonacionId(), ranking);
  }

  public Optional<Ranking> buscarPorDonacion(String donacionId) {
    return Optional.ofNullable(rankingsPorDonacion.get(donacionId));
  }

  public void eliminar(String donacionId) {
    rankingsPorDonacion.remove(donacionId);
  }
}
