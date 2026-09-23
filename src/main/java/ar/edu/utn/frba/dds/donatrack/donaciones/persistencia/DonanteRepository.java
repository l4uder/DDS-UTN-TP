package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class DonanteRepository implements WithSimplePersistenceUnit {
  private static final DonanteRepository INSTANCE = new DonanteRepository();

  private DonanteRepository() { }

  public static DonanteRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Donante donante) {
    entityManager().persist(donante);
  }

  public Donante buscarPorId(Long id) {
    return entityManager().find(Donante.class, id);
  }

  public List<Donante> buscarPorTipoPersona(String tipo) {
    return entityManager().createQuery("SELECT d FROM Donante d WHERE d.tipo = :tipoDonante", Donante.class)
        .setParameter("tipoDonante", tipo.toUpperCase())
        .getResultList();
  }

  public List<Donante> buscarAusentesPorMas(Integer dias) {
    /* todo hasta tener persistido las entregas
    LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
    return entityManager().createQuery("SELECT d FROM Donante d JOIN d.entregas e GROUP BY d.id HAVING MAX(e.fecha) < :fechaLimite", Donante.class)
        .setParameter("fechaLimite", fechaLimite)
        .getResultList();

     */
    return List.of();
  }

  public List<Donante> buscarTodos() {
    return entityManager().createQuery("SELECT d FROM Donante d", Donante.class).getResultList();
  }

  public void actualizar(Donante donante) {
    if (donante.getId() == null || buscarPorId(donante.getId()) == null) {
      throw new RegistroNoEncontradoException("No se puede actualizar: no existe en la base de datos el donante: " + donante.getId());
    }
    entityManager().merge(donante);
  }

  public void eliminar(Donante donante) {
    entityManager().remove(donante);
  }

}
