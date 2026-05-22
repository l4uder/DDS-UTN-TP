package ar.edu.utn.frba.dds.donatrack.notificacion.servicioWhatsapp;

public class WhatsappMock implements ServicioWhatsapp {
    @Override
    public void notificar(String numero, String mensaje) {
        System.out.println("Se envio un whasapp a: " + numero + "con el siguiente mensaje: " + mensaje);
    }
}
