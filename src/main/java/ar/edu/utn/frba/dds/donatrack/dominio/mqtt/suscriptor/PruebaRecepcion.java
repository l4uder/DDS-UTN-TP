package ar.edu.utn.frba.dds.donatrack.dominio.mqtt.suscriptor;

public class PruebaRecepcion {
  public static void main(String[] args) {
    System.out.println("El tp hace cosas ...");
    /** El tp hace cosas ... */

    EstacionRecepcion receptor = new EstacionRecepcion();
    receptor.conectar();
    receptor.suscribir("g7/dds/vehiculos/gps/ubicaciones/+");

    System.out.println("El tp sigue haciendo cosas ...");
    /** El tp sigue haciendo cosas ... */
    pasanMin(3);
    System.out.println("El tp decide cerrar la conexion ...");
    receptor.desconectar();
  }

  //=============Funciones auxiliares=================
  private static void pasanMin(Integer minutos) {
    try {
      Thread.sleep(minutos*1000*60);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
