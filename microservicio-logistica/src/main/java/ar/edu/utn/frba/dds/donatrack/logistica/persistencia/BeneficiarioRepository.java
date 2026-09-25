package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;

import java.util.List;

public class BeneficiarioRepository implements WithLogisticaPersistenceUnit {
  private static final BeneficiarioRepository INSTANCE = new BeneficiarioRepository();

  private BeneficiarioRepository() { }

  public static BeneficiarioRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Beneficiario beneficiario) {
    withTransaction(() -> entityManager().persist(beneficiario));
  }

  public Beneficiario buscarPorId(Long id) {
    return entityManager().find(Beneficiario.class, id);
  }

  public List<Beneficiario> buscarTodos() {
    return entityManager().createQuery("SELECT b FROM Beneficiario b", Beneficiario.class)
        .getResultList();
  }

  public void actualizar(Beneficiario beneficiario) {
    if (beneficiario.getId() == null || buscarPorId(beneficiario.getId()) == null) {
      throw new RegistroNoEncontradoException(
          "No se puede actualizar: no existe beneficiario con id " + beneficiario.getId());
    }
    withTransaction(() -> entityManager().merge(beneficiario));
  }

  public void eliminar(Long id) {
    Beneficiario beneficiario = buscarPorId(id);
    if (beneficiario == null) {
      throw new RegistroNoEncontradoException("No existe beneficiario con id " + id);
    }
    withTransaction(() -> entityManager().remove(beneficiario));
  }
}