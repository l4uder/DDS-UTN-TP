package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BaseDatoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CamionRepository implements WithSimplePersistenceUnit {
  private static final CamionRepository INSTANCE = new CamionRepository();

  private CamionRepository() { }

  public static CamionRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Camion camion) {
    withTransaction(() -> entityManager().persist(camion));
  }

  public Camion buscarPorPatente(String patente) {
    return entityManager().find(Camion.class, patente);
  }

  public Camion buscarPorGps(String idGps) {
    return entityManager().createQuery(
            "SELECT c FROM Camion c WHERE UPPER(c.gps.imei) = UPPER(:imei)", Camion.class)
        .setParameter("imei", idGps)
        .getResultList().stream().findFirst().orElse(null);
  }

  public List<Camion> buscarTodos() {
    return entityManager().createQuery("SELECT c FROM Camion c", Camion.class)
        .getResultList();
  }

  public void actualizar(Camion camion) {
    if (camion.getPatente() == null || buscarPorPatente(camion.getPatente()) == null) {
      throw new RegistroNoEncontradoException(
          "No se puede actualizar: no existe en la base de datos la patente " + camion.getPatente());
    }
    withTransaction(() -> entityManager().merge(camion));
  }

  public void eliminar(String patente) {
    Camion camion = buscarPorPatente(patente);
    if (camion == null) {
      throw new RegistroNoEncontradoException("No existe camión con patente " + patente);
    }
    withTransaction(() -> entityManager().remove(camion));
  }
}