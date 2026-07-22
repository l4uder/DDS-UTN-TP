package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class CamionRepository {
  private static final CamionRepository INSTANCE = new CamionRepository();
  private Map<String, Camion> camionesStore;

  private CamionRepository() {
    camionesStore = new HashMap<>();
  }

  public static CamionRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Camion camion) {
    camionesStore.put(camion.getPatente(), camion);
  }

  public Optional<Camion> buscarPorPatente(String patente) {
    return Optional.ofNullable(camionesStore.get(patente));
  }

  public Camion obtenerPorPatente(String patente) {
    return buscarPorPatente(patente)
        .orElseThrow(() -> new RecursoNoEncontradoException(
            "No existe camion con patente " + patente));
  }

  public void insertar(Camion camion) {
    if (camionesStore.containsKey(camion.getPatente())) {
      throw new DomainValidationException(
          "Ya existe un camion con patente " + camion.getPatente());
    }
    camionesStore.put(camion.getPatente(), camion);
  }

  public void eliminar(String patente) {
    camionesStore.remove(patente);
  }

  public List<Camion> buscarTodos() {
    return camionesStore.values().stream().toList();
  }

  public Camion buscarCamionPorGps(String idGps) {
    return camionesStore.values().stream()
        .filter(c -> c.posee(idGps))
        .findFirst()
        .orElseThrow(() -> new DomainValidationException(
        "No existe camion con ese gps "));
  }

}
