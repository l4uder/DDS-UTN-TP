package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class ClienteWhatsappRealTwilio implements ClienteWhatsapp {
    private final String numeroOrigen;

    public ClienteWhatsappRealTwilio(String accountSid, String authToken, String numeroOrigen) {
        Twilio.init(accountSid, authToken); 
        this.numeroOrigen = "whatsapp:" + numeroOrigen;
    }

    @Override
    public void enviarMensaje(String numeroDestino, String mensaje) {
        Message.creator(
            new PhoneNumber("whatsapp:" + numeroDestino),
            new PhoneNumber(this.numeroOrigen),
            mensaje
        ).create();
    }
}