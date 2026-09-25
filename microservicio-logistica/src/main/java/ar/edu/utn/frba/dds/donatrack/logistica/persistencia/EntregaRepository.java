package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BaseDatoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.*;

public class EntregaRepository implements WithLogisticaPersistenceUnit {
  private static final EntregaRepository INSTANCE = new EntregaRepository();

  private EntregaRepository() { }

  public static EntregaRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Entrega entrega) {
    withTransaction(() -> entityManager().persist(entrega));
  }

  public Entrega buscarPorId(Long id) {
    return entityManager().find(Entrega.class, id);
  }

  public List<Entrega> buscarTodas() {
    return entityManager().createQuery("SELECT e FROM Entrega e", Entrega.class)
        .getResultList();
  }

  public void actualizar(Entrega entrega) {
    if (entrega.getId() == null || buscarPorId(entrega.getId()) == null) {
      throw new RegistroNoEncontradoException(
          "No se puede actualizar: no existe en la base de datos la entrega: " + entrega.getId());
    }
    withTransaction(() -> entityManager().merge(entrega));
  }

  public void eliminar(Long id) {
    Entrega entrega = buscarPorId(id);
    if (entrega == null) {
      throw new RegistroNoEncontradoException("No existe entrega con id " + id);
    }
    withTransaction(() -> entityManager().remove(entrega));
  }
}