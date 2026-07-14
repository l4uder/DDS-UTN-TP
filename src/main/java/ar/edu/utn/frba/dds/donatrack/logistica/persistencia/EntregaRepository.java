package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;

import java.util.*;

public class EntregaRepository {
  private static final EntregaRepository INSTANCE = new EntregaRepository();
  private final Map<String, Entrega> store = new HashMap<>();

  private EntregaRepository() {}

  public static EntregaRepository getInstancia() {
    return EntregaRepository.INSTANCE;
  }

  public void guardar(Entrega entrega) {
    store.put(entrega.getId(), entrega);
  }

  public Entrega buscarPorId(String id) {
    Entrega entrega = store.get(id);
    if (entrega == null) {
      throw new RecursoNoEncontradoException("Entrega no encontrada: " + id);
    }
    return entrega;
  }

  public void eliminar(String id) {store.remove(id);}

  public List<Entrega> buscarTodas() {
    return new ArrayList<>(store.values());
  }
}