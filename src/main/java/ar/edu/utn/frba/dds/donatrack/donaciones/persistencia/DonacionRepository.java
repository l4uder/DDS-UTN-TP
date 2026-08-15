package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BaseDatoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DonacionRepository {
  private static final DonacionRepository INSTANCE = new DonacionRepository();
  private final Map<String, Donacion> storeDonaciones;

  private DonacionRepository() {
    storeDonaciones = new HashMap<>();
  }

  public static DonacionRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Donacion donacion) {
    if (donacion.getId() != null) throw new BaseDatoException("Constraint Violations: La donación ya tiene un ID asignado: " + donacion.getId());

    donacion.setId(UUID.randomUUID().toString());
    this.storeDonaciones.put(donacion.getId(), donacion);
  }

  public Donacion buscarPorId(String id) {
    return storeDonaciones.get(id);
  }

  public List<Donacion> buscarTodoPorEstado(TipoEstadoDonacion estado) {
    return this.storeDonaciones.values().stream()
        .filter(d -> d.getEstadoActual() == estado).toList();
  }

  public List<Donacion> buscarTodos() {
    return this.storeDonaciones.values().stream().toList();
  }

  public void actualizar(Donacion donacion) {
    if (donacion.getId() == null || !this.storeDonaciones.containsKey(donacion.getId())) {
      throw new RegistroNoEncontradoException("No se puede actualizar: no existe en la base de datos la donación: " + donacion.getId());
    }
    this.storeDonaciones.put(donacion.getId(), donacion);
  }

  public void eliminar(Donacion donacion) {
    storeDonaciones.remove(donacion.getId());
  }

}
