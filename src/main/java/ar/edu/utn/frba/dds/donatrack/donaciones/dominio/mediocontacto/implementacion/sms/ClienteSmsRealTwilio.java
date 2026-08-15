package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms;

import ar.edu.utn.frba.dds.donatrack.shared.ConfiguracionEntorno;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class ClienteSmsRealTwilio implements ClienteSms {
    private final String numeroOrigen;

    public ClienteSmsRealTwilio() {
        Twilio.init(ConfiguracionEntorno.getInstance().getTwilioAccountSid(), ConfiguracionEntorno.getInstance().getTwilioAuthToken());
        this.numeroOrigen = ConfiguracionEntorno.getInstance().getTwilioSmsNumber();
    }

    @Override
    public void enviarSms(String numeroDestino, String mensaje) {
        Message.creator(
            new PhoneNumber(numeroDestino),
            new PhoneNumber(this.numeroOrigen),
            mensaje
        ).create();
    }
}