package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BaseDatoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CamionRepository {
  private static final CamionRepository INSTANCE = new CamionRepository();
  private final Map<String, Camion> camionesStore;

  private CamionRepository() {
    camionesStore = new HashMap<>();
  }

  public static CamionRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Camion camion) {
    if (camionesStore.containsKey(camion.getPatente())) {
      throw new BaseDatoException("Constraint Violations: Ya existe un camion con patente " + camion.getPatente());
    }
    camionesStore.put(camion.getPatente(), camion);
  }

  public Camion buscarPorPatente(String patente) {
    return camionesStore.get(patente);
  }

  public Camion buscarPorGps(String idGps) {
    return camionesStore.values().stream()
        .filter(c -> c.posee(idGps))
        .findFirst()
        .orElse(null);
  }

  public List<Camion> buscarTodos() {
    return camionesStore.values().stream().toList();
  }

  public void actualizar(Camion camion) {
    if (camion.getPatente() != null && !camionesStore.containsKey(camion.getPatente())) {
      throw new RegistroNoEncontradoException("No se puede actualizar: no existe en la base de datos la patente " + camion.getPatente());
    }
    camionesStore.put(camion.getPatente(), camion);
  }

  public void eliminar(Camion camion) {
    camionesStore.remove(camion.getPatente());
  }

}
