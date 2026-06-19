package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.Representante;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.TelefonoDeContato;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.WhatsappDeContato;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.implementacion.ClienteCorreo;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.implementacion.ClienteSms;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.implementacion.ClienteWhatsapp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotificadorTest {
    PersonaHumana persona;
    PersonaJuridica constructoraSRL;

    @BeforeEach
    void configuracionInicial() {
        persona = new PersonaHumana(
                "Juan",
                "Pérez",
                new Documento(TipoDocumento.DNI, "45123456"),
                LocalDate.of(2000, 5, 10),
                Genero.MASCULINO,
                "Av. Corrientes 1234",
                new CorreoDeContato("pepe@gmail.com"),
                List.of()
        );

        Representante rep = new Representante(
                "Carlos",
                "García",
                LocalDate.of(2000, 5, 12),
                new Documento(TipoDocumento.DNI, "30123456"),
                Genero.MASCULINO,
                "Av. San Martín 100",
                new CorreoDeContato("carlos@srl.com"),
                List.of()
        );

        constructoraSRL = new PersonaJuridica(
                "Constructora Junior SRL",
                TipoOrganizacion.EMPRESA,
                "Construcción",
                new Documento(TipoDocumento.CUIT, "30-12345678-9"),
                List.of(rep),
                new CorreoDeContato("srl@gmail.com"),
                List.of()
        );
    }

    @Test
    void notificarAUnaPersonaHumanaConUnCorreo() {
        ClienteCorreo client = mock(ClienteCorreo.class);
        String message = "Hola..";
        ((CorreoDeContato)persona.getMedioDeContactoPred()).setClienteCorreo(client);
        persona.getMedioDeContactoPred().notificar(message);

        verify(client).enviarCorreo(((CorreoDeContato)persona.getMedioDeContactoPred()).getCorreo(), message);
    }

    @Test
    void notificarAUnaPersonaHumanaConVariosCorreos() {
        String message = "Hola...!!";
        String correo1 = "SRLSecundario@gmail.com";
        String correo2 = "SRLerceraa@gmail.com";
        ClienteCorreo client = mock(ClienteCorreo.class);
        persona.agregarContactoSecundario(new CorreoDeContato(correo1));
        persona.agregarContactoSecundario(new CorreoDeContato(correo2));
        persona.getMediosDeContacto().stream().filter(el->el instanceof CorreoDeContato).forEach(el -> {
            ((CorreoDeContato)el).setClienteCorreo(client);
            el.notificar(message);
        });

        verify(client).enviarCorreo(correo1, message);
        verify(client).enviarCorreo(correo2, message);
    }

    @Test
    void notificarAUnaPersonaHumanaPorWhashapp() {
        String message = "Hola...!";
        String tel = "235254543";
        ClienteWhatsapp client = mock(ClienteWhatsapp.class);
        var medioDeContacto = new WhatsappDeContato(tel);
        medioDeContacto.setClienteWhatsapp(client);
        persona.agregarContactoSecundario(medioDeContacto);
        persona.getMediosDeContacto().stream().filter(el->el instanceof WhatsappDeContato).forEach(el -> el.notificar(message));

        verify(client).enviarMensaje(tel, message);
    }

    @Test
    void notificarAUnaPersonaJuridicaPorSMS() {
        String message = "Hola...";
        String tel = "434644456";
        ClienteSms client = mock(ClienteSms.class);
        var medioDeContacto = new TelefonoDeContato(tel);
        medioDeContacto.setClienteSms(client);
        persona.agregarContactoSecundario(medioDeContacto);
        persona.getMediosDeContacto().stream().filter(el->el instanceof TelefonoDeContato).forEach(el -> el.notificar(message));

        verify(client).enviarSms(tel, message);
    }
}
