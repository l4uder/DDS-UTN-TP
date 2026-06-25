package ar.edu.utn.frba.dds.donatrack.persistencia;

import ar.edu.utn.frba.dds.donatrack.dominio.donante.Donante;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DonanteRepository {
  private static final DonanteRepository INSTANCE = new DonanteRepository();
  private Map<String, Donante> donantesStore;

  private DonanteRepository() {
    donantesStore = new HashMap<>();
  }

  public static DonanteRepository getInstancia() {
    return INSTANCE;
  }

  public void guardarDonante(Donante donante) {
    donantesStore.put(donante.getEmail(), donante);
  }

  public List<Donante> buscarAusentesPorMas(Integer dias) {
    return donantesStore.values().stream().filter(d -> d.estaAusentePorMasDe(dias)).toList();
  }

  public List<Donante> buscarTodos() {
    return donantesStore.values().stream().toList();
  }
}
