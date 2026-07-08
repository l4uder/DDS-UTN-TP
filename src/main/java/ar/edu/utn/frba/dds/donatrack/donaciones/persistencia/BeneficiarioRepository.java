package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
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

  public Optional<Beneficiario> buscarPorId(String id) {
    return Optional.ofNullable(beneficiariosStore.get(id));
  }

  public void eliminar(String id) {
    beneficiariosStore.remove(id);
  }

  public List<Beneficiario> buscarTodos() {
    return beneficiariosStore.values().stream().toList();
  }
}
