package ar.edu.utn.frba.dds.donatrack.logistica.broker;

import ar.edu.utn.frba.dds.donatrack.logistica.broker.suscriptor.EstacionRecepcion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Gps;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.GpsRepository;

public class PruebaRecepcion {
  public static void main(String[] args) {
    CamionRepository repoCamiones = CamionRepository.getInstancia();
    GpsRepository repoGps = GpsRepository.getInstancia();
    Camion camionA = new Camion("vvv 4353", 34.5f, 45f, 80f);
    Camion camionB = new Camion("aaa 4353", 34.5f, 45f, 80f);
    Gps gpsA = new Gps("000A");
    Gps gpsB = new Gps("000B");

    camionA.agregarGps(gpsA);
    camionB.agregarGps(gpsB);
    repoGps.guardar(gpsA);
    repoGps.guardar(gpsB);
    repoCamiones.guardar(camionA);
    repoCamiones.guardar(camionB);

    System.out.println("camionA esta en: " + camionA.getUbicacionActual());
    System.out.println("camionB esta en: " + camionB.getUbicacionActual());

    //============== DESDE ACA COMIENZA =======================
    EstacionRecepcion receptor = new EstacionRecepcion(repoCamiones, repoGps);
    receptor.conectar();
    receptor.suscribir("Donatrack/g7/dds/gps/ubicaciones/+");

    pasanMin(4);
    receptor.desconectar();
    System.out.printf("El camion A esta en: [%s : %s] ", camionA.getUbicacionActual().getLatitud(), camionA.getUbicacionActual().getLongitud());
    System.out.printf("El camion B esta en: [%s : %s] ", camionB.getUbicacionActual().getLatitud(), camionB.getUbicacionActual().getLongitud());
  }

  //=============Funciones auxiliares=================
  private static void pasanMin(Integer minutos) {
    try {
      Thread.sleep(minutos * 1000 * 60);
    } catch (InterruptedException e) {
      System.out.println("error al esperar minutos " + e.getMessage());
    }
  }

}
