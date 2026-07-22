package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDonante;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class DonanteRepository {
  private static final DonanteRepository INSTANCE = new DonanteRepository();
  private final Map<String, Donante> storeDonantes;

  private DonanteRepository() {
    storeDonantes = new HashMap<>();
  }

  public static DonanteRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Donante donante) {
    if (donante.getId() != null) throw new IllegalArgumentException("Constraint Violations: " + "El donante ya tiene un ID asignado: " + donante.getId());

    donante.setId(UUID.randomUUID().toString());
    this.storeDonantes.put(donante.getId(), donante);
  }

  public Donante buscarPorId(String id) {
    return storeDonantes.get(id);
  }

  public List<Donante> buscarPorTipo(TipoDonante tipo) {
    return storeDonantes.values().stream()
        .filter(d -> d.getTipo() == tipo)
        .toList();
  }

  public List<Donante> buscarTodos() {
    return storeDonantes.values().stream().toList();
  }

  public Optional<Donante> buscarPorEmail(String email) {
    return storeDonantes.values().stream()
        .filter(d -> d.buscarEmail().map(e -> e.equalsIgnoreCase(email)).orElse(false))
        .findFirst();
  }

  public List<Donante> buscarAusentesPorMas(Integer dias) {
    return storeDonantes.values().stream().filter(d -> d.estaAusentePorMasDe(dias)).toList();
  }

  public void actualizar(Donante donante) {
    if (donante.getId() == null || !this.storeDonantes.containsKey(donante.getId())) {
      throw new IllegalArgumentException("El beneficiario No existe en la base de dato");
    }
    this.storeDonantes.put(donante.getId(), donante);
  }

  public void eliminar(Donante donante) {
    storeDonantes.remove(donante.getId());
  }

}
