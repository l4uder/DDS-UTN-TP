package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class CategoriaRepository implements WithSimplePersistenceUnit {
  private static final CategoriaRepository INSTACE = new CategoriaRepository();

  private CategoriaRepository() { }

  public static CategoriaRepository getInstancia() {
    return INSTACE;
  }

  public void guardar(Categoria categoria) {
    entityManager().persist(categoria);
  }

  public Categoria buscarPorNombre(String nombre) {
    return entityManager()
        .createQuery("SELECT c FROM Categoria c WHERE c.nombre = :nombre", Categoria.class)
        .setParameter("nombre", nombre.toLowerCase())
        .getResultStream()
        .findFirst()
        .orElse(null);
  }
}
