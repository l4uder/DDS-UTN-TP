package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Gps;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.List;

public class GpsRepository implements WithSimplePersistenceUnit {
  private static final GpsRepository INSTANCE = new GpsRepository();

  private GpsRepository() { }

  public static GpsRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Gps gps) {
    withTransaction(() -> entityManager().persist(gps));
  }

  public Gps buscarPorId(String imei) {
    return entityManager().find(Gps.class, imei);
  }

  public List<Gps> buscarTodos() {
    return entityManager().createQuery("SELECT g FROM Gps g", Gps.class)
        .getResultList();
  }

  public void actualizar(Gps gps) {
    if (gps.getImei() == null || buscarPorId(gps.getImei()) == null) {
      throw new RegistroNoEncontradoException(
          "No se puede actualizar: no existe gps con imei " + gps.getImei());
    }
    withTransaction(() -> entityManager().merge(gps));
  }
}
