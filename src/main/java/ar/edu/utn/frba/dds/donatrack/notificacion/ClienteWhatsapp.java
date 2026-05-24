package ar.edu.utn.frba.dds.donatrack.notificacion;

public class ClienteWhatsapp {
    public void enviarMensaje(String numeroWhatsapp, String mensaje){
        System.out.println("Se envio un whasapp a: " + numeroWhatsapp + " con el siguiente mensaje: " + mensaje);
    }
}
