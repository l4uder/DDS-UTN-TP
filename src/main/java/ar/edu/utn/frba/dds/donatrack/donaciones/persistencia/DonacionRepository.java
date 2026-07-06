package ar.edu.utn.frba.dds.donatrack.donaciones.persistencia;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DonacionRepository {
  private static DonacionRepository INSTANCE = new DonacionRepository();
  private Map<String, Donacion> donaciones;

  private DonacionRepository() {
    donaciones = new HashMap<>();
  }

  public static DonacionRepository getInstancia() {
    return INSTANCE;
  }

  public void guardarDonacion(Donacion donacion) {
    if (donacion.getId() == null) {
      donacion.setId(UUID.randomUUID().toString());
    }
    this.donaciones.put(donacion.getId(), donacion);
  }

  public Optional<Donacion> buscarPorId(String id) {
    return Optional.ofNullable(donaciones.get(id));
  }

  public void eliminar(String id) {
    donaciones.remove(id);
  }

  public List<Donacion> buscarPorEstado(TipoEstadoDonacion estado) {
    return this.donaciones.values().stream()
            .filter(d -> d.getEstadoActual() == estado).toList();
  }

  public List<Donacion> buscarDonacionesEnDeposito() {
    return buscarPorEstado(TipoEstadoDonacion.EN_DEPOSITO);
  }

  public List<Donacion> buscarTodos() {
    return this.donaciones.values().stream().toList();
  }
}
