package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.*;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.*;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.TipoEstadoEntrega;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntregaTest {

  private Beneficiario beneficiario;
  private DonacionEnTransito donacion;
  private Camion camion;
  private Entrega entrega;

  private Donante donantePrueba;

  @BeforeEach
  void setUp() {
    WhatsappDeContato contactoWhatsapp =
        new WhatsappDeContato("132212212");

    CorreoDeContato contactoCorreo = 
        new CorreoDeContato("comedor@prueba.com");

    List<MedioContacto> listaContactos =
        List.of(contactoCorreo); //Cambio a correo a la espera de la implementación de envio por Whatsapp

    beneficiario = new Beneficiario("ben-1", "Comedor San José", "Av. Siempre Viva 123");

    donacion = new DonacionEnTransito("don-1", "Fideos", beneficiario);

    camion = new Camion("AB123CD", 10f, 2.5f, 1500f);
    camion = new Camion("AB123CD", 10f, 2.5f, 1500f);

    //Mock de Motor de correos exclusivo para los tests
    ProveedorClienteCorreo.inicializar((destino, mensaje) -> {
        // No hace nada de red, solo simula que lo envió
        System.out.println("TEST - Simulando envío a: " + destino);
    });

    donantePrueba = new PersonaHumanaBuilder()
      .conNombre("Juan")
      .conApellido("Pérez")
      .conDocumento(new Documento(TipoDocumento.DNI, "12345678"))
      .conContactoPrincipal(new CorreoDeContato("juan@prueba.com"))
      .build();

    //donacion.confirmarAsignacion(beneficiario);

    entrega = new Entrega(beneficiario, List.of(donacion), camion);
  }

  @Test
  void unaEntregaNuevaQuedaPendiente() {
    assertEquals(TipoEstadoEntrega.PENDIENTE, entrega.getEstadoActual());
  }

  @Test
  void confirmarListaParaEntregarNoCambiaEstadoDeEntrega() {
    entrega.confirmarListaParaEntregar();
    assertEquals(TipoEstadoEntrega.PENDIENTE, entrega.getEstadoActual());
  }

  @Test
  void iniciarTrasladoCambiaEstadoDeEntrega() {
    entrega.iniciarTraslado();
    assertEquals(TipoEstadoEntrega.EN_TRASLADO, entrega.getEstadoActual());
  }

  @Test
  void confirmarRecepcionMarcaEntregaComoEntregada() {
    entrega.iniciarTraslado();
    entrega.confirmarRecepcion();
    assertEquals(TipoEstadoEntrega.ENTREGADA, entrega.getEstadoActual());
  }

  @Test
  void marcarNoRecibidaActualizaEstadoDeEntrega() {
    entrega.iniciarTraslado();
    entrega.marcarNoRecibida("Nadie respondió en el domicilio");
    assertEquals(TipoEstadoEntrega.NO_RECIBIDA, entrega.getEstadoActual());
  }

  @Test
  void reingresarADepositoVuelveEntregaAPendiente() {
    entrega.iniciarTraslado();
    entrega.marcarNoRecibida("Incidente logístico");
    entrega.reingresarDeposito();
    assertEquals(TipoEstadoEntrega.PENDIENTE, entrega.getEstadoActual());
  }

  @Test
  void noSePuedeReingresarSiNoEstaNoRecibida() {
    assertThrows(IllegalStateException.class, () -> entrega.reingresarDeposito());
  }

  @Test
  void agregarFotoRecepcionRechazaValoresVacios() {
    entrega.confirmarListaParaEntregar();
    entrega.iniciarTraslado();
    entrega.confirmarRecepcion();

    assertThrows(DomainValidationException.class, () -> entrega.agregarFotoRecepcion(""));
    assertThrows(DomainValidationException.class, () -> entrega.agregarFotoRecepcion(null));
    assertTrue(entrega.getFotosRecepcion().isEmpty());

    entrega.agregarFotoRecepcion("https://storage.donatrack.com/foto1.jpg");
    assertFalse(entrega.getFotosRecepcion().isEmpty());
  }

  @Test
  void agregarFotoRecepcionRechazaSiLaEntregaNoFueConfirmada() {
    assertThrows(IllegalStateException.class,
        () -> entrega.agregarFotoRecepcion("https://storage.donatrack.com/foto1.jpg"));
  }
}
