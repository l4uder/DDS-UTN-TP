package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Gps;
import java.util.ArrayList;
import java.util.List;

public class GpsRepository {
  private static GpsRepository INSTANCE = new GpsRepository();
  private List<Gps> gpsList;

  private GpsRepository() {
    this.gpsList = new ArrayList<>();
  }

  public static GpsRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Gps gps) {
    this.gpsList.add(gps);
  }

  public Gps buscarPorId(String id) {
    return this.gpsList.stream()
        .filter(g -> g.getImei().equalsIgnoreCase(id))
        .findFirst()
        .orElseThrow(() -> new DominioException("No existe gps con ese id "));
  }

  public List<Gps> buscarTodos() {
    return this.gpsList;
  }

}
