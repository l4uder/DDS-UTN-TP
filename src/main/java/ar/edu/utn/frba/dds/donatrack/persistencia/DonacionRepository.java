package ar.edu.utn.frba.dds.donatrack.persistencia;

import ar.edu.utn.frba.dds.donatrack.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.dominio.donacion.TipoEstadoDonacion;
import java.util.ArrayList;
import java.util.List;

public class DonacionRepository {
  private static DonacionRepository INSTANCE = new DonacionRepository();
  private List<Donacion> donaciones;

  private DonacionRepository() {
    donaciones = new ArrayList<>();
  }

  public static DonacionRepository getInstancia() {
    return INSTANCE;
  }

  public void guardarDonacion(Donacion donacion) {
    this.donaciones.add(donacion);
  }

  public List<Donacion> buscarDonacionesEnDeposito() {
    return this.donaciones.stream()
            .filter(d -> d.getEstadoActual() == TipoEstadoDonacion.EN_DEPOSITO).toList();
  }

  public List<Donacion> buscarTodos() {
    return this.donaciones;
  }
}

