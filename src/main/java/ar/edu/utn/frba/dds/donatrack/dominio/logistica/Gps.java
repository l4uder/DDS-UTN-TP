package ar.edu.utn.frba.dds.donatrack.dominio.logistica;

import java.util.ArrayList;
import java.util.List;

public class Gps {
  private String id;
  private List<Ubicacion> ubicaciones;

  public Gps(String id) {
    this.id = id;
    this.ubicaciones = new ArrayList<>();
  }

  public void agregarUbicacion(Ubicacion ubicacion) {
    this.ubicaciones.add(ubicacion);
  }

  public Ubicacion getUbicacion() {
    return this.ubicaciones.get(this.ubicaciones.size() -1);
  }
}
