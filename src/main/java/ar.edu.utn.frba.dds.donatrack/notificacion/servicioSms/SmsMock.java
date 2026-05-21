package ar.edu.utn.frba.dds.donatrack.notificacion.servicioSms;

public class SmsMock implements ServicioSMS{

    @Override
    public void notificar(Integer numero, String mensaje) {
        System.out.println("Se envio un sms a: " + numero.toString() + "con el siguiente: " + mensaje);
    }
}

