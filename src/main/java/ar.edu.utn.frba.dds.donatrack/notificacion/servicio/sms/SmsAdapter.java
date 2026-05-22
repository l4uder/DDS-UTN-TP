package ar.edu.utn.frba.dds.donatrack.notificacion.servicio.sms;

public class SmsAdapter implements ServicioSms {
  @Override
  public void notificar(String numero, String mensaje) {
     //Esta clase se conecta con el servicio externo y enviara el mensaje
  }
}
