package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta.Ruta;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BaseDatoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import java.util.*;

public class RutaRepository {
  private static final RutaRepository INSTANCE = new RutaRepository();
  private final Map<String, Ruta> storeRuta;

  private RutaRepository() {
    this.storeRuta = new HashMap<>();
  }

  public static RutaRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Ruta ruta) {
    if (ruta.getId() != null) {
      throw new BaseDatoException("Constraint Violations: La ruta ya tiene un ID asignado: " + ruta.getId());
    }
    ruta.setId(UUID.randomUUID().toString());
    this.storeRuta.put(ruta.getId(), ruta);
  }

  public Ruta buscarPorId(String id) {
    return storeRuta.get(id);
  }

  public List<Ruta> buscarTodas() {
    return new ArrayList<>(storeRuta.values());
  }

  public void actualizar(Ruta ruta) {
    if (ruta.getId() == null || !this.storeRuta.containsKey(ruta.getId())) {
      throw new RegistroNoEncontradoException("No se puede actualizar: no existe en la base de datos la ruta: " + ruta.getId());
    }
    this.storeRuta.put(ruta.getId(), ruta);
  }

  public void eliminar(Ruta ruta) {
    storeRuta.remove(ruta.getId());
  }

}