package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BaseDatoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DonacionRepository implements WithSimplePersistenceUnit {
  private static final DonacionRepository INSTANCE = new DonacionRepository();
  private DonacionRepository() { }
  public void guardar(Donacion donacion) {
    entityManager().persist(donacion);
  }

  public Donacion buscarPorId(Long id) {                 // era String
    return entityManager().find(Donacion.class, id);
  }
  public static DonacionRepository getInstancia() {
    return INSTANCE;
  }

  public List<Donacion> buscarTodoPorEstado(TipoEstadoDonacion estado) {
    return buscarTodos().stream().filter(d -> d.getEstadoActual() == estado).toList();
  }

  public List<Donacion> buscarTodos() {
    return entityManager()
        .createQuery("SELECT d FROM Donacion d", Donacion.class)
        .getResultList();
  }

  public void actualizar(Donacion donacion) {
    if (donacion.getId() == null || buscarPorId(donacion.getId()) == null) {
      throw new RegistroNoEncontradoException("No se puede actualizar: no existe en la base de datos la donacion: " + donacion.getId());
    }
    entityManager().merge(donacion);
  }

  public void eliminar(Donacion donacion) {
   entityManager().remove(donacion);
  }

}
