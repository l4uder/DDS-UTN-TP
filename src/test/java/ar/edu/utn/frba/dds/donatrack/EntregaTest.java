package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.EntidadBeneficiariaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaJuridicaBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.TipoEstadoEntrega;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.WhatsappDeContato;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EntregaTest {

  private Beneficiario beneficiario;
  private Donacion donacion;
  private Camion camion;
  private Entrega entrega;

  @BeforeEach
  void setUp() {
    WhatsappDeContato contactoWhatsapp =
        new WhatsappDeContato("132212212");

    List<MedioContacto> listaContactos =
        List.of(contactoWhatsapp);

    beneficiario = new EntidadBeneficiariaBuilder()
        .conRazonSocial("Comedor San José")
        .conDireccion("Av. Siempre Viva 123")
        .conMediosContactos(listaContactos)
        .build();

    donacion = new Donacion(List.of(
        new BienBuilder()
            .conDescripcion("Fideos")
            .conCantidad(5)
            .conUsado(false)
            .buildNoPerecedero()
    ));

    donacion.confirmarAsignacion(beneficiario);

    camion = new Camion("AB123CD", 10f, 2.5f, 1500f);

    entrega = new Entrega(
        beneficiario,
        List.of(donacion),
        camion
    );
  }

  @Test
  void unaEntregaNuevaQuedaPendiente() {
    assertEquals(TipoEstadoEntrega.PENDIENTE, entrega.getEstadoActual());
  }

  @Test
  void confirmarListaParaEntregarActualizaLasDonaciones() {
    entrega.confirmarListaParaEntregar();
    assertEquals(TipoEstadoDonacion.LISTA_PARA_ENTREGAR, donacion.getEstadoActual());
  }

  @Test
  void iniciarTrasladoCambiaEntregaYDonaciones() {
    entrega.confirmarListaParaEntregar();
    entrega.iniciarTraslado();

    assertEquals(TipoEstadoEntrega.EN_TRASLADO, entrega.getEstadoActual());
    assertEquals(TipoEstadoDonacion.EN_TRASLADO, donacion.getEstadoActual());
  }

  @Test
  void confirmarRecepcionMarcaEntregaYDonacionesComoEntregadas() {
    entrega.confirmarListaParaEntregar();
    entrega.iniciarTraslado();
    entrega.confirmarRecepcion();

    assertEquals(TipoEstadoEntrega.ENTREGADA, entrega.getEstadoActual());
    assertEquals(TipoEstadoDonacion.ENTREGADA, donacion.getEstadoActual());
  }

  @Test
  void marcarNoRecibidaPropagaMotivoADonaciones() {
    entrega.confirmarListaParaEntregar();
    entrega.iniciarTraslado();
    entrega.marcarNoRecibida("Nadie respondió en el domicilio");

    assertEquals(TipoEstadoEntrega.NO_RECIBIDA, entrega.getEstadoActual());
    assertEquals(TipoEstadoDonacion.ENTREGA_FALLIDA, donacion.getEstadoActual());
  }

  @Test
  void reingresarADepositoVuelveDonacionesADeposito() {
    entrega.confirmarListaParaEntregar();
    entrega.iniciarTraslado();
    entrega.marcarNoRecibida("Incidente logístico");
    entrega.reingresarDeposito();

    assertEquals(TipoEstadoEntrega.PENDIENTE, entrega.getEstadoActual());
    assertEquals(TipoEstadoDonacion.EN_DEPOSITO, donacion.getEstadoActual());
  }

  @Test
  void noSePuedeReingresarSiNoEstaNoRecibida() {
    assertThrows(IllegalStateException.class, () -> entrega.reingresarDeposito());
  }

  @Test
  void agregarFotoRecepcionIgnoraValoresVacios() {
    entrega.agregarFotoRecepcion("");
    entrega.agregarFotoRecepcion(null);
    assertFalse(entrega.tieneFotos());

    entrega.agregarFotoRecepcion("https://storage.donatrack.com/foto1.jpg");
    assertTrue(entrega.tieneFotos());
  }
}
