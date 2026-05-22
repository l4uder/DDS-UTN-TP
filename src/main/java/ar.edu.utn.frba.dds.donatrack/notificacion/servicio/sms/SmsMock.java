package ar.edu.utn.frba.dds.donatrack.notificacion.servicio.sms;

public class SmsMock implements ServicioSms {
  @Override
  public void notificar(String numero, String mensaje) {
    System.out.println("Se envio un sms a: " + numero + " con el siguiente: " + mensaje);
  }
}

