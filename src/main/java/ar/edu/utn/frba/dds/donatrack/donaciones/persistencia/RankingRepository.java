package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.Ranking;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BaseDatoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RankingRepository implements WithSimplePersistenceUnit {
  private static final RankingRepository INSTANCE = new RankingRepository();

  private RankingRepository() {}

  public static RankingRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Ranking ranking) {
    entityManager().persist(ranking);
  }

  public Ranking buscarPorId(Long id) {
    return entityManager().find(Ranking.class, id);
  }

  public List<Ranking> buscarTodos() {
    return entityManager().createQuery("SELECT r FROM Ranking r", Ranking.class).getResultList();
  }

  public void actualizar(Ranking ranking) {
    if (ranking.getId() == null || buscarPorId(ranking.getId()) == null) {
      throw new RegistroNoEncontradoException("No se puede actualizar: no existe en la base de datos el registro: " + ranking.getId());
    }
    entityManager().merge(ranking);
  }

  public void vaciarSoft() {
    entityManager().createQuery("UPDATE Ranking r SET r.estaVigente = false").executeUpdate();
  }
}
