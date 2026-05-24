package ar.edu.utn.frba.dds.donatrack.contacto;

public sealed interface MedioContacto permits CorreoDeContato, TelefonoDeContato, WhatsappDeContato {
    void notificar(String message);
}
