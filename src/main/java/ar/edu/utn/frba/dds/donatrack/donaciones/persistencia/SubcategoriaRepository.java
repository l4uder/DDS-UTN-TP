package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class SubcategoriaRepository implements WithSimplePersistenceUnit {
  private static final SubcategoriaRepository INSTANCE = new SubcategoriaRepository();

  private SubcategoriaRepository() { }

  public static SubcategoriaRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Subcategoria subcategoria) {
    entityManager().persist(subcategoria);
  }

  public Subcategoria buscarPorNombre(String nombre) {
    return entityManager()
        .createQuery("SELECT s FROM Subcategoria s WHERE s.nombre = :nombre", Subcategoria.class)
        .setParameter("nombre", nombre.toLowerCase())
        .getResultStream()
        .findFirst()
        .orElse(null);
  }

}
