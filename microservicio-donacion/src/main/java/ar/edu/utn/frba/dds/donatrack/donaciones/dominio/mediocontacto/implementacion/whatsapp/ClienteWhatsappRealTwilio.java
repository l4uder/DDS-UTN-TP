package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp;

import ar.edu.utn.frba.dds.donatrack.shared.ConfiguracionEntorno;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class ClienteWhatsappRealTwilio implements ClienteWhatsapp {
    private final String numeroOrigen;

    public ClienteWhatsappRealTwilio() {
        Twilio.init(ConfiguracionEntorno.getInstance().getTwilioAccountSid(), ConfiguracionEntorno.getInstance().getTwilioAuthToken());
        this.numeroOrigen = "whatsapp:" + ConfiguracionEntorno.getInstance().getTwilioWhatsappNumber();
    }

    @Override
    public void enviarWhatsapp(String numeroDestino, String mensaje) {
        Message.creator(
            new PhoneNumber("whatsapp:" + numeroDestino),
            new PhoneNumber(this.numeroOrigen),
            mensaje
        ).create();
    }
}