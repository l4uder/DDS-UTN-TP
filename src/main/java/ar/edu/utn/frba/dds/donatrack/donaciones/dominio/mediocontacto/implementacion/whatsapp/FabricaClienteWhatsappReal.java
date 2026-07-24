package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp;
import ar.edu.utn.frba.dds.donatrack.shared.ConfiguracionEntorno;

public class FabricaClienteWhatsappReal {
    public static ClienteWhatsappRealTwilio start() {
        ConfiguracionEntorno config = ConfiguracionEntorno.getInstance();
        return new ClienteWhatsappRealTwilio(
            config.getTwilioAccountSid(), 
            config.getTwilioAuthToken(), 
            config.getTwilioWhatsappNumber()
        );
    }
}