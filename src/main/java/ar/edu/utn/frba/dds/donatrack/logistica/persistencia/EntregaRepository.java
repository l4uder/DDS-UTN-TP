package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BaseDatoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import java.util.*;

public class EntregaRepository {
  private static final EntregaRepository INSTANCE = new EntregaRepository();
  private final Map<String, Entrega> storeEntrega;

  private EntregaRepository() {
    this.storeEntrega = new HashMap<>();
  }

  public static EntregaRepository getInstancia() {
    return EntregaRepository.INSTANCE;
  }

  public void guardar(Entrega entrega) {
    if (entrega.getId() != null) {
      throw new BaseDatoException("Constraint Violations: La entrega ya tiene un ID asignado: " + entrega.getId());
    }
    entrega.setId(UUID.randomUUID().toString());
    this.storeEntrega.put(entrega.getId(), entrega);
  }


  public Entrega buscarPorId(String id) {
    return storeEntrega.get(id);
  }

  public List<Entrega> buscarTodas() {
    return new ArrayList<>(storeEntrega.values());
  }

  public void actualizar(Entrega entrega) {
    if (entrega.getId() == null || !this.storeEntrega.containsKey(entrega.getId())) {
      throw new RegistroNoEncontradoException("No se puede actualizar: no existe en la base de datos la entrega: " + entrega.getId());
    }
    this.storeEntrega.put(entrega.getId(), entrega);
  }

  public void eliminar(Entrega entrega) {
    storeEntrega.remove(entrega.getId());
  }

}