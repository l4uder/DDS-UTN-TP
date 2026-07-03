package ar.edu.utn.frba.dds.donatrack.persistencia;

import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Camion;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Gps;
import java.util.ArrayList;
import java.util.List;

public class CamionRepository {
  private static CamionRepository INSTANCE = new CamionRepository();
  private List<Camion> camiones;

  private CamionRepository() {
    camiones = new ArrayList<>();
  }

  public static CamionRepository getInstancia() {
    return INSTANCE;
  }

  public void guardar(Camion camion) {
    this.camiones.add(camion);
  }

  public Camion buscarCamionPorGps(String idGps) {
    return this.camiones.stream()
        .filter(c -> c.posee(idGps))
        .findFirst()
        .orElseThrow(() -> new DomainValidationException(
        "No existe camion con ese gps "));
  }

  public List<Camion> buscarTodos() {
    return camiones;
  }

}
