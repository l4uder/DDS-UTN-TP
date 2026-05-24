package ar.edu.utn.frba.dds.donatrack.contacto;

import ar.edu.utn.frba.dds.donatrack.notificacion.ClienteWhatsapp;

public final class WhatsappDeContato extends TelefonoDeContato implements MedioContacto {

    private ClienteWhatsapp clienteWhatsapp;

    public WhatsappDeContato(String telefono) {
        super(telefono);
    }

    @Override
    public void notificar(String message) {
        clienteWhatsapp.enviarMensaje(super.telefono, message);
    }

    public void setClienteWhatsapp(ClienteWhatsapp clienteWhatsapp) {
        this.clienteWhatsapp = clienteWhatsapp;
    }
}
