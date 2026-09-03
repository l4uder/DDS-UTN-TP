package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BaseDatoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BeneficiarioRepository implements WithSimplePersistenceUnit {
  private static final BeneficiarioRepository INSTANCE = new BeneficiarioRepository();

  private BeneficiarioRepository() { }

  public static BeneficiarioRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Beneficiario beneficiario) {
    entityManager().persist(beneficiario);
  }

  public Beneficiario buscarPorId(Long id) {
    return entityManager().find(Beneficiario.class, id);
    /* es equivalente a:
    return entityManager().createQuery("SELECT b FROM Beneficiario b WHERE b.id = :id", Beneficiario.class)
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);  */
  }

  public List<Beneficiario> buscarTodos() {
    return entityManager().createQuery("SELECT b FROM Beneficiario b", Beneficiario.class).getResultList();
  }

  public void actualizar(Beneficiario beneficiario) {
    if (beneficiario.getId() == null || buscarPorId(beneficiario.getId()) == null) {
      throw new RegistroNoEncontradoException("No se puede actualizar: no existe en la base de datos el beneficiario: " + beneficiario.getId());
    }
    entityManager().merge(beneficiario);
  }

  public void eliminar(Beneficiario beneficiario) {
    entityManager().remove(beneficiario);
  }

}
