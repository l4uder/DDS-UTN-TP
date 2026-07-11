package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Ruta;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;

import java.util.*;

public class RutaRepository {
  private static final RutaRepository INSTANCE = new RutaRepository();
  private final Map<String, Ruta> store = new HashMap<>();

  private RutaRepository() {}

  public static RutaRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Ruta ruta) {
    store.put(ruta.getId(), ruta);
  }

  public Ruta buscarPorId(String id) {
    Ruta ruta = store.get(id);
    if (ruta == null) {
      throw new RecursoNoEncontradoException("Ruta no encontrada: " + id);
    }
    return ruta;
  }

  public List<Ruta> buscarTodas() {
    return new ArrayList<>(store.values());
  }

  public void eliminar(String id) {
    if (!store.containsKey(id)) {
      throw new RecursoNoEncontradoException("Ruta no encontrada: " + id);
    }
    store.remove(id);
  }
}