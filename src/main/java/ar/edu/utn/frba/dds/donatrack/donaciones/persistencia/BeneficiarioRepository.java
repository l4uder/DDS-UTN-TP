package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class BeneficiarioRepository {
  private static final BeneficiarioRepository INSTANCE = new BeneficiarioRepository();
  private Map<String, Beneficiario> beneficiariosStore;

  private BeneficiarioRepository() {
    beneficiariosStore = new HashMap<>();
  }

  public static BeneficiarioRepository getInstancia() {
    return INSTANCE;
  }

  public void guardarBeneficiario(Beneficiario beneficiario) {
    if (beneficiario.getId() == null) {
      beneficiario.setId(UUID.randomUUID().toString());
    }
    beneficiariosStore.put(beneficiario.getId(), beneficiario);
  }

  public Beneficiario buscarPorId(String id) {
    return beneficiariosStore.get(id);
  }

  public Beneficiario obtenerPorId(String id) {
    Beneficiario beneficiario = beneficiariosStore.get(id);
    if (beneficiario == null) {
      throw new RecursoNoEncontradoException("No existe beneficiario con id " + id);
    }
    return beneficiario;
  }

  public void actualizar(Beneficiario beneficiario) {
    if (beneficiario.getId() == null || !this.beneficiariosStore.containsKey(beneficiario.getId())) {
      throw new IllegalArgumentException("El beneficiario No existe en la base de dato");
    }
    this.beneficiariosStore.put(beneficiario.getId(), beneficiario);
  }

  public void eliminar(String id) {
    beneficiariosStore.remove(id);
  }

  public List<Beneficiario> buscarTodos() {
    return beneficiariosStore.values().stream().toList();
  }
}
