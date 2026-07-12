package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.Ranking;
import java.util.HashMap;
import java.util.List;
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

  public Ranking buscarPorDonacion(Donacion donacion) {
    return rankingsPorDonacion.get(donacion.getId());
  }

  public List<Ranking> buscarTodos() {
    return rankingsPorDonacion.values().stream().toList();
  }

  public void eliminar(String donacionId) {
    rankingsPorDonacion.remove(donacionId);
  }
}
