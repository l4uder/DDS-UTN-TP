package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.*;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.*;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.*;
import ar.edu.utn.frba.dds.donatrack.builder.*;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.TipoEstadoEntrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.BeneficiarioDTO;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.DonacionAsignadaDTO;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class EntregaTest {
  private Beneficiario beneficiario;
  private Donacion donacion;
  private Camion camion;
  private Entrega entrega;
  private Donante donantePrueba;

  @BeforeEach
  void setUp() {
    MedioContacto contactoWhatsapp = new WhatsappDeContato("132212212");
    MedioContacto contactoCorreo = new CorreoDeContato("comedor@prueba.com");
    List<MedioContacto> listaContactos = List.of(contactoCorreo, contactoWhatsapp);

    beneficiario = new BeneficiarioBuilder()
        .conRazonSocial("Comedor San José")
        .conDireccion("Av. Siempre Viva 123")
        .conAgregarContacto(contactoWhatsapp)
        .build();

    Bien bien = new BienBuilder().conDescripcion("Arroz").conCantidad(3).conUsado(false).buildNoPerecedero();

    Donante donante = new PersonaHumanaBuilder()
        .conNombre("Juan")
        .conApellido("Pérez")
        .conDocumento(new Documento(TipoDocumento.DNI, "12345678"))
        .conContactoPrincipal(new CorreoDeContato("juan@prueba.com"))
        .build();

    donacion = new Donacion(List.of(bien), List.of(donante));
    donacion.confirmarAsignacion(beneficiario);

    camion = new Camion("AB123CD", 10f, 2.5f, 1500f);
    camion = new Camion("AB123CD", 10f, 2.5f, 1500f);

    donantePrueba = new PersonaHumanaBuilder()
      .conNombre("Juan")
      .conApellido("Pérez")
      .conDocumento(new Documento(TipoDocumento.DNI, "12345678"))
      .conContactoPrincipal(new CorreoDeContato("juan@prueba.com"))
      .build();

    entrega = new Entrega(beneficiario, List.of(donacion), camion);
  }

  @Test
  void unaEntregaNuevaQuedaPendiente() {
    assertEquals(TipoEstadoEntrega.PENDIENTE, entrega.getEstadoActual());
  }

  @Test
  void confirmarListaParaEntregarCambiaEstadoDeEntrega() {
    entrega.confirmarListaParaEntregar();
    assertEquals(TipoEstadoEntrega.LISTA_PARA_ENTREGAR, entrega.getEstadoActual());
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
  void noSePuedeAgregarFotoDeValoresVacios() {
    assertThrows(DomainValidationException.class, () -> entrega.agregarFotoRecepcion(""));
  }
}
