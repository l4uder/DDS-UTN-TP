package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donante.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donante.TipoContacto;
import ar.edu.utn.frba.dds.donatrack.donante.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.notificacion.CorreoElectronico;
import ar.edu.utn.frba.dds.donatrack.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.notificacion.Telefono;
import ar.edu.utn.frba.dds.donatrack.notificacion.Whatsapp;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicioCorreo.CorreoMock;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicioSms.SmsMock;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicioWhatsapp.WhatsappMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class NotificadorTest {
    PersonaHumana pepe;
    PersonaJuridica constructoraSRL;
    @BeforeEach
    void configuracionInicial() {
        pepe = new PersonaHumana("Juan",
                "Pérez",
                new Documento(TipoDocumento.DNI, "45123456"),
                LocalDate.of(2000, 5, 10),
                Genero.MASCULINO,
                "Av. Corrientes 1234",
                new MedioContacto(TipoContacto.CORREO, "pepe@gmail.com"),
                null);

        constructoraSRL = new PersonaJuridica("constructora junior SRL",
                TipoOrganizacion.EMPRESA,
                "algun rubro",
                null,
                new MedioContacto(TipoContacto.CORREO, "SRLassa@gmail.com"),
                null);
    }

    @Test
    void notificarAUnaPersonaHumanaConUnCorreo() {
        CorreoElectronico envioPorCorreo = new CorreoElectronico(new CorreoMock());

        envioPorCorreo.notificar(pepe, "debe hacer .... algoo");
    }

    @Test
    void notificarAUnaPersonaHumanaConVariosCorreos() {
        pepe.agregarContactoSecundario(new MedioContacto(TipoContacto.CORREO, "SRLSecundario@gmail.com"));
        pepe.agregarContactoSecundario(new MedioContacto(TipoContacto.CORREO, "SRLerceraa@gmail.com"));

        CorreoElectronico envioPorCorreo = new CorreoElectronico(new CorreoMock());

        envioPorCorreo.notificar(pepe, "debe hacer .... algoo");
    }

    @Test
    void notificarAUnaPersonaHumanaPorWhashapp() {
        pepe.agregarContactoSecundario(new MedioContacto(TipoContacto.WHATSAPP, "434644456"));

        Whatsapp envioPorWashapp = new Whatsapp(new WhatsappMock());

        envioPorWashapp.notificar(pepe, "debe hacer .... algoo");
    }

    @Test
    void notificarAUnaPersonaJuridicaPorSMS() {
        pepe.agregarContactoSecundario(new MedioContacto(TipoContacto.TELEFONO, "434644456"));

        Telefono envioPorTelefono = new Telefono(new SmsMock());

        envioPorTelefono.notificar(pepe, "debe hacer .... algoo");
    }
}
