package ar.edu.utn.frba.dds.donatrack.persistencia;

import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Gps;
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
        .orElseThrow(() -> new DomainValidationException("No existe gps con ese id "));
  }

  public List<Gps> buscarTodos() {
    return this.gpsList;
  }

}
