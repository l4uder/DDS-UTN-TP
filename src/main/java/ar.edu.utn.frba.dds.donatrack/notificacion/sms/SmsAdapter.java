package ar.edu.utn.frba.dds.donatrack.notificacion.servicioSms;

public class SmsAdapter implements ServicioSMS{
    @Override
    public void notificar(String numero, String mensaje) {
        //Esta clase se conecta con el servicio externo y enviara el mensaje
    }
}
