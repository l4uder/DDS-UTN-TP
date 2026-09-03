package ar.edu.utn.frba.dds.donatrack.logistica.persistencia;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Gps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class DatosIniciales implements WithSimplePersistenceUnit {
  public static void init() {
    new DatosIniciales().comenzar();
  }

  public void comenzar() {
    CamionRepository repoCamiones = CamionRepository.getInstancia();
    Camion camion = new Camion("ABC-123", 10f, 2.5f, 1500f);
    Gps gps = new Gps("000A");

    camion.agregarGps(gps);

    beginTransaction();
    repoCamiones.guardar(camion);
    commitTransaction();
  }

}