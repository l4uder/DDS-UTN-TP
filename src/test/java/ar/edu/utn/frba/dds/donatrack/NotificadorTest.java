package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ProveedorClienteCorreo;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaJuridicaBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.SmsDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.WhatsappDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ClienteCorreo;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ClienteSms;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ClienteWhatsapp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotificadorTest {
  PersonaHumanaBuilder buildPersona;
  PersonaJuridicaBuilder buildPersonaJuridica;
  PersonaJuridica constructoraSRL;
  ClienteCorreo clienteMockCorreo;
  ClienteSms clienteMockSms;
  ClienteWhatsapp clienteMockWhatsapp;
  String message;

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

    Representante rep = new Representante(
            "Carlos",
            "García",
            LocalDate.of(2000, 5, 12),
            new Documento(TipoDocumento.DNI, "30123456"),
            Genero.MASCULINO,
            "Av. San Martín 100",
            new CorreoDeContato("carlos@srl.com")
    );

    buildPersonaJuridica = new PersonaJuridicaBuilder().conRazonSocial("Constructora Junior SRL")
        .conTipoOrganizacion(TipoOrganizacion.EMPRESA)
        .conRubro("Construcción")
        .conDocumento(new Documento(TipoDocumento.CUIT, "30-12345678-9"))
        .conRepresentantes(List.of(rep));

    message = "Hola..";

    //Mock de Motor de correos exclusivo para los tests
    ProveedorClienteCorreo.inicializar((destino, mensaje) -> {
        // No hace nada de red, solo simula que lo envió
        System.out.println("TEST - Simulando envío a: " + destino);
    });
  }

  @Test
  void notificarAUnaPersonaHumanaPorCorreo() {
    CorreoDeContato correoJuan = new CorreoDeContato("juanpepe@gmail.com");
    correoJuan.setClienteCorreo(clienteMockCorreo);
    PersonaHumana juan = buildPersona.conContactoPrincipal(correoJuan).build();

    juan.recibirNotificacion(message);

    verify(clienteMockCorreo).enviarCorreo(correoJuan.getCorreo(), message);
  }

  @Test
  void notificarAUnaPersonaJuridicaPorSMS() {
    SmsDeContato numeroSms = new SmsDeContato("434644456");
    numeroSms.setClienteSms(clienteMockSms);
    PersonaJuridica constructoraSRL = buildPersonaJuridica.conContactoPrincipal(numeroSms).build();

    constructoraSRL.recibirNotificacion(message);

    verify(clienteMockSms).enviarSms(numeroSms.getTelefono(), message);
  }

  @Test
  void notificarAUnaPersonaHumanaPorWhashapp() {
    WhatsappDeContato numeroWhatsapp = new WhatsappDeContato("235254543");
    numeroWhatsapp.setClienteWhatsapp(clienteMockWhatsapp);
    PersonaHumana juan = buildPersona.conContactoPrincipal(numeroWhatsapp).build();

    juan.recibirNotificacion(message);

    verify(clienteMockWhatsapp).enviarMensaje(numeroWhatsapp.getTelefono(), message);
  }

  @Test
  void notificarAUnaPersonaHumanaConVariosCorreos() {
    CorreoDeContato correo1 = new CorreoDeContato("juanpepe@gmail.com");
    CorreoDeContato correo2 = new CorreoDeContato("juanSecundario@gmail.com");
    correo1.setClienteCorreo(clienteMockCorreo);
    correo2.setClienteCorreo(clienteMockCorreo);
    PersonaHumana juan = buildPersona.conContactoPrincipal(correo1).conContactoSecundario(correo2).build();

    juan.recibirNotificacion(message);

    verify(clienteMockCorreo).enviarCorreo(correo1.getCorreo(), message);
    verify(clienteMockCorreo).enviarCorreo(correo2.getCorreo(), message);
  }

}
