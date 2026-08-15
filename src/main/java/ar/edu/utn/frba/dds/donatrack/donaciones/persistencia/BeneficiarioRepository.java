package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BaseDatoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BeneficiarioRepository {
  private static final BeneficiarioRepository INSTANCE = new BeneficiarioRepository();
  private final Map<String, Beneficiario> storeBeneficiarios;

  private BeneficiarioRepository() {
    storeBeneficiarios = new HashMap<>();
  }

  public static BeneficiarioRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Beneficiario beneficiario) {
    if (beneficiario.getId() != null) throw new BaseDatoException("Constraint Violations: El beneficiario ya tiene un ID asignado: " + beneficiario.getId());
    beneficiario.setId(UUID.randomUUID().toString());

    this.storeBeneficiarios.put(beneficiario.getId(), beneficiario);
  }

  public Beneficiario buscarPorId(String id) {
    return storeBeneficiarios.get(id);
  }

  public List<Beneficiario> buscarTodos() {
    return storeBeneficiarios.values().stream().toList();
  }

  public void actualizar(Beneficiario beneficiario) {
    if (beneficiario.getId() == null || !this.storeBeneficiarios.containsKey(beneficiario.getId())) {
      throw new RegistroNoEncontradoException("No se puede actualizar: no existe en la base de datos el beneficiario: " + beneficiario.getId());
    }
    this.storeBeneficiarios.put(beneficiario.getId(), beneficiario);
  }

  public void eliminar(Beneficiario beneficiario) {
    storeBeneficiarios.remove(beneficiario.getId());
  }
}
