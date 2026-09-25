package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta.Chofer;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;

import java.util.List;

public class ChoferRepository implements WithLogisticaPersistenceUnit {
  private static final ChoferRepository INSTANCE = new ChoferRepository();

  private ChoferRepository() { }

  public static ChoferRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Chofer chofer) {
    withTransaction(() -> entityManager().persist(chofer));
  }

  public Chofer buscarPorLicencia(String licenciaConducir) {
    return entityManager().find(Chofer.class, licenciaConducir);
  }

  public List<Chofer> buscarTodos() {
    return entityManager().createQuery("SELECT c FROM Chofer c", Chofer.class)
        .getResultList();
  }

  public void actualizar(Chofer chofer) {
    if (chofer.getLicenciaConducir() == null || buscarPorLicencia(chofer.getLicenciaConducir()) == null) {
      throw new RegistroNoEncontradoException(
          "No se puede actualizar: no existe chofer con licencia " + chofer.getLicenciaConducir());
    }
    withTransaction(() -> entityManager().merge(chofer));
  }

  public void eliminar(String licenciaConducir) {
    Chofer chofer = buscarPorLicencia(licenciaConducir);
    if (chofer == null) {
      throw new RegistroNoEncontradoException("No existe chofer con licencia " + licenciaConducir);
    }
    withTransaction(() -> entityManager().remove(chofer));
  }
}