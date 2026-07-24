package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms;
import ar.edu.utn.frba.dds.donatrack.shared.ConfiguracionEntorno;

public class FabricaClienteSmsReal {
    public static ClienteSmsRealTwilio start() {
        ConfiguracionEntorno config = ConfiguracionEntorno.getInstance();
        return new ClienteSmsRealTwilio(
            config.getTwilioAccountSid(), 
            config.getTwilioAuthToken(), 
            config.getTwilioSmsNumber()
        );
    }
}