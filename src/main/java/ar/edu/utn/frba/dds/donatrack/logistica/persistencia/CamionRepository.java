package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Gps;
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
