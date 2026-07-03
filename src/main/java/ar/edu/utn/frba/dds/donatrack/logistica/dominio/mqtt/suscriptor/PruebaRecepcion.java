package ar.edu.utn.frba.dds.donatrack.logistica.dominio.mqtt.suscriptor;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Gps;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.GpsRepository;

public class PruebaRecepcion {
  public static void main(String[] args) {
    System.out.println("El tp hace cosas ...");
    Camion camionA = new Camion("vvv 4353", 34.5f, 45f, 80f);
    Camion camionB = new Camion("aaa 4353", 34.5f, 45f, 80f);
    Gps gpsA = new Gps("gpsA");
    Gps gpsB = new Gps("gpsB");
    camionA.agregarGps(gpsA);
    camionB.agregarGps(gpsB);

    CamionRepository repoCamiones = CamionRepository.getInstancia();
    GpsRepository repoGps = GpsRepository.getInstancia();
    repoCamiones.guardar(camionA);
    repoCamiones.guardar(camionB);
    repoGps.guardar(gpsA);
    repoGps.guardar(gpsB);

    System.out.println("camionA esta en: " + camionA.getUbicacionActual());
    System.out.println("camionB esta en: " + camionB.getUbicacionActual());
    /* El tp hace cosas ... */


    EstacionRecepcion receptor = new EstacionRecepcion(repoCamiones, repoGps);
    receptor.conectar();
    receptor.suscribir("g7/dds/vehiculos/gps/ubicaciones/+");
    /* El tp sigue haciendo cosas ... */


    System.out.println("El tp sigue haciendo cosas ...");
    pasanMin(4);
    System.out.println("El tp decide cerrar la conexión ...");
    receptor.desconectar();

    System.out.println("camionA esta en: " + camionA.getUbicacionActual());
    System.out.println("camionB esta en: " + camionB.getUbicacionActual());
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
