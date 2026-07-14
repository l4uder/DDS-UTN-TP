package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    if (donante.getId() == null) {
      donante.buscarEmail().flatMap(this::buscarPorEmail)
          .ifPresent(existente -> donante.setId(existente.getId()));
    }
    if (donante.getId() == null) {
      donante.setId(UUID.randomUUID().toString());
    }
    donantesStore.put(donante.getId(), donante);
  }

  public Optional<Donante> buscarPorId(String id) {
    return Optional.ofNullable(donantesStore.get(id));
  }

  public Donante obtenerPorId(String id) {
    return buscarPorId(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("No existe donante con id " + id));
  }

  public Optional<Donante> buscarPorEmail(String email) {
    return donantesStore.values().stream()
        .filter(d -> d.buscarEmail().map(e -> e.equalsIgnoreCase(email)).orElse(false))
        .findFirst();
  }

  public void eliminar(String id) {
    donantesStore.remove(id);
  }

  public List<Donante> buscarAusentesPorMas(Integer dias) {
    return donantesStore.values().stream().filter(d -> d.estaAusentePorMasDe(dias)).toList();
  }

  public List<Donante> buscarTodos() {
    return donantesStore.values().stream().toList();
  }
}
