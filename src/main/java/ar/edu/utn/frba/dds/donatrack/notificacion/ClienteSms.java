package ar.edu.utn.frba.dds.donatrack.notificacion;

public class ClienteSms {
    public void enviarSms(String numeroDeTelefono, String mensaje){
        System.out.println("Se envio un sms a: " + numeroDeTelefono + " con el siguiente: " + mensaje);
    }
}
