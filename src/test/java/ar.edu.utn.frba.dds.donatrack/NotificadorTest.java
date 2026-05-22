package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.donante.Representante;
import ar.edu.utn.frba.dds.donatrack.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import ar.edu.utn.frba.dds.donatrack.donante.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.notificacion.CorreoElectronico;
import ar.edu.utn.frba.dds.donatrack.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.notificacion.Telefono;
import ar.edu.utn.frba.dds.donatrack.notificacion.Whatsapp;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicio.correo.CorreoMock;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicio.sms.SmsMock;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicio.whatsapp.WhatsappMock;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
            new MedioContacto(TipoContacto.CORREO, "pepe@gmail.com"),
            null);

        Representante rep = new Representante(
            "Carlos",
            "García",
            LocalDate.of(2000, 5, 12),
            new Documento(TipoDocumento.DNI, "30123456"),
            Genero.MASCULINO,
            "Av. San Martín 100",
            new MedioContacto(TipoContacto.CORREO, "carlos@srl.com"),
            null);

        constructoraSRL = new PersonaJuridica(
            "Constructora Junior SRL",
            TipoOrganizacion.EMPRESA,
            "Construcción",
            List.of(rep),
            new MedioContacto(TipoContacto.CORREO, "srl@gmail.com"),
            null);
    }

    @Test
    void notificarAUnaPersonaHumanaConUnCorreo() {
        CorreoMock mock = new CorreoMock();
        CorreoElectronico envioPorCorreo = new CorreoElectronico(mock);

        envioPorCorreo.notificar(persona, "debe hacer .... algoo");
    }

    @Test
    void notificarAUnaPersonaHumanaConVariosCorreos() {
        persona.agregarContactoSecundario(new MedioContacto(TipoContacto.CORREO, "SRLSecundario@gmail.com"));
        persona.agregarContactoSecundario(new MedioContacto(TipoContacto.CORREO, "SRLerceraa@gmail.com"));

        CorreoElectronico envioPorCorreo = new CorreoElectronico(new CorreoMock());

        envioPorCorreo.notificar(persona, "debe hacer .... algoo");
    }

    @Test
    void notificarAUnaPersonaHumanaPorWhashapp() {
        persona.agregarContactoSecundario(new MedioContacto(TipoContacto.WHATSAPP, "434644456"));

        Whatsapp envioPorWashapp = new Whatsapp(new WhatsappMock());

        envioPorWashapp.notificar(persona, "debe hacer .... algoo");
    }

    @Test
    void notificarAUnaPersonaJuridicaPorSMS() {
        constructoraSRL.agregarContactoSecundario(new MedioContacto(TipoContacto.TELEFONO, "434644456"));

        Telefono envioPorTelefono = new Telefono(new SmsMock());

        envioPorTelefono.notificar(constructoraSRL, "debe hacer .... algoo");
    }
}
