package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.TipoEstadoEntrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.BeneficiarioDTO;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.DonacionAsignadaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntregaTest {

  private BeneficiarioDTO beneficiario;
  private DonacionAsignadaDTO donacion;
  private Camion camion;
  private Entrega entrega;

  @BeforeEach
  void setUp() {
    beneficiario = new BeneficiarioDTO("ben-1", "Comedor San José", "Av. Siempre Viva 123");

    donacion = new DonacionAsignadaDTO("don-1", "Fideos", beneficiario);

    camion = new Camion("AB123CD", 10f, 2.5f, 1500f);

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
  void agregarFotoRecepcionIgnoraValoresVacios() {
    entrega.agregarFotoRecepcion("");
    entrega.agregarFotoRecepcion(null);
    assertFalse(entrega.tieneFotos());

    entrega.agregarFotoRecepcion("https://storage.donatrack.com/foto1.jpg");
    assertTrue(entrega.tieneFotos());
  }
}
