package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaJuridicaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.RepresentanteBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Juridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.SmsDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.WhatsappDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo.ClienteCorreo;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms.ClienteSms;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp.ClienteWhatsapp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotificadorTest {
  PersonaHumanaBuilder buildPersona;
  PersonaJuridicaBuilder buildPersonaJuridica;
  Juridica constructoraSRL;
  ClienteCorreo clienteMockCorreo;
  ClienteSms clienteMockSms;
  ClienteWhatsapp clienteMockWhatsapp;
  String message;
  RepresentanteBuilder repBuild;

  @BeforeEach
  void configuracionInicial() {
    clienteMockCorreo = mock(ClienteCorreo.class);
    clienteMockWhatsapp = mock(ClienteWhatsapp.class);
    clienteMockSms = mock(ClienteSms.class);

    buildPersona = new PersonaHumanaBuilder().conNombre("Juan").conApellido("Pérez")
        .conDocumento(new Documento(TipoDocumento.DNI, "45123456"))
        .conFechaNacimiento(LocalDate.of(2000, 5, 10))
        .conGenero(Genero.MASCULINO)
        .conDireccion("Av. Corrientes 1234");

    repBuild = new RepresentanteBuilder()
        .conNombre("representanteA");
        //.conAgregarContacto(new CorreoDeContato("carlos@srl.com", true))
        //.build();

    buildPersonaJuridica = new PersonaJuridicaBuilder().conRazonSocial("Constructora Junior SRL")
        .conTipoOrganizacion(TipoOrganizacion.EMPRESA)
        .conRubro("Construcción")
        .conDocumento(new Documento(TipoDocumento.CUIT, "30-12345678-9"));
        //.conRepresentantes(List.of(representante));

    message = "Hola..";
  }

  @Test
  void notificarAUnaPersonaHumanaPorCorreo() {
    CorreoDeContato correoJuan = new CorreoDeContato("juanpepe@gmail.com", true, clienteMockCorreo);
    Donante juan = buildPersona.conAgregarContacto(correoJuan).build();

    juan.recibirNotificacion(message);

    verify(clienteMockCorreo).enviarCorreo(correoJuan.getCorreo(), message);
  }

  @Test
  void notificarAUnaPersonaJuridicaPorSMS() {
    SmsDeContato numeroSms = new SmsDeContato("434644456", true, clienteMockSms);
    Donante constructoraSRL = buildPersonaJuridica.conAgregarRepresetante(repBuild.conAgregarContacto(numeroSms).build()).build();

    constructoraSRL.recibirNotificacion(message);

    verify(clienteMockSms).enviarSms(numeroSms.getTelefono(), message);
  }

  @Test
  void notificarAUnaPersonaHumanaPorWhashapp() {
    WhatsappDeContato numeroWhatsapp = new WhatsappDeContato("235254543", true, clienteMockWhatsapp);
    Donante juan = buildPersona.conAgregarContacto(numeroWhatsapp).build();

    juan.recibirNotificacion(message);

    verify(clienteMockWhatsapp).enviarWhatsapp(numeroWhatsapp.getTelefono(), message);
  }

  @Test
  void notificarAUnaPersonaHumanaConVariosCorreos() {
    CorreoDeContato correo1 = new CorreoDeContato("juanpepe@gmail.com", true, clienteMockCorreo);
    CorreoDeContato correo2 = new CorreoDeContato("juanSecundario@gmail.com", true, clienteMockCorreo);

    Donante juan = buildPersona.conAgregarContacto(correo1).conAgregarContacto(correo2).build();

    juan.recibirNotificacion(message);

    verify(clienteMockCorreo).enviarCorreo(correo1.getCorreo(), message);
    verify(clienteMockCorreo).enviarCorreo(correo2.getCorreo(), message);
  }

}
