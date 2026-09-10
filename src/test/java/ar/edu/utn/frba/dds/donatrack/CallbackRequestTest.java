package ar.edu.utn.frba.dds.donatrack;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.TipoEstadoEntrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion.Lote;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.RutaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.web.coordinadores.CoordinadorRuta;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.planificacion.CallbackPlanificacionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.microserviciosdonaciones.ConectorDonacionesApi;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.planificadorexterno.ClientePlanificadorExterno;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoordinadorRutaFlujoCompletoTest {

  private static final String CALLBACK_URL_ESPERADA = "http://localhost:7071/planificaciones/callback-externo";

  private CoordinadorRuta coordinador;
  private ConectorDonacionesApi donacionesClient;
  private ClientePlanificadorExterno clienteExterno;
  private RutaRepository rutaRepository;
  private CamionRepository camionRepository;
  private EntregaRepository entregaRepository;
  private BeneficiarioRepository beneficiarioRepository;

  @BeforeEach
  void setUp() {
    rutaRepository = RutaRepository.getInstancia();
    camionRepository = CamionRepository.getInstancia();
    entregaRepository = EntregaRepository.getInstancia();
    beneficiarioRepository = BeneficiarioRepository.getInstancia();
    donacionesClient = mock(ConectorDonacionesApi.class);
    clienteExterno = mock(ClientePlanificadorExterno.class);

    coordinador = new CoordinadorRuta(
        rutaRepository, camionRepository, entregaRepository, beneficiarioRepository, donacionesClient, clienteExterno);
  }

  @Test
  void planificaYProcesaElCallbackDeUnCamionConEntregasAsignadas() {
    Camion camion = new Camion("AB123CD", 10f, 2.5f, 1500f);
    camionRepository.guardar(camion);

    Beneficiario beneficiario = new Beneficiario("ben-1", "Comedor A", "Calle 1");
    DonacionEnTransito donacion = new DonacionEnTransito("don-1", "Fideos", beneficiario);
    when(donacionesClient.buscarDonacionesAsignadas()).thenReturn(List.of(donacion));

    List<Entrega> entregas = coordinador.planificarEntregasPendientes();
    assertEquals(1, entregas.size());
    String idEntrega = entregas.get(0).getId();
    assertEquals(TipoEstadoEntrega.PENDIENTE, entregas.get(0).getEstadoActual());

    CallbackPlanificacionRequest request = new CallbackPlanificacionRequest(
        Map.of("AB123CD", List.of(idEntrega)),
        List.of(),
        LocalDate.now().toString()
    );
    List<Ruta> rutas = coordinador.procesarCallback(request);

    assertEquals(1, rutas.size());
    assertEquals("AB123CD", rutas.get(0).getCamion().getPatente());
    assertEquals(1, rutas.get(0).getEntregasOrdenadas().size());

    Entrega entregaActualizada = entregaRepository.buscarPorId(idEntrega);
    assertEquals(TipoEstadoEntrega.LISTA_PARA_ENTREGAR, entregaActualizada.getEstadoActual());
    assertEquals("AB123CD", entregaActualizada.getCamionAsignado().getPatente());

    verify(donacionesClient).marcarDonacionListaParaEntregar("don-1");
  }

  @Test
  void lasEntregasSinAsignarSeEliminanDelRepositorio() {
    Camion camion = new Camion("XY111AA", 8f, 2f, 1000f);
    camionRepository.guardar(camion);

    Beneficiario beneficiario = new Beneficiario("ben-2", "Comedor B", "Calle 2");
    DonacionEnTransito donacion = new DonacionEnTransito("don-2", "Arroz", beneficiario);
    when(donacionesClient.buscarDonacionesAsignadas()).thenReturn(List.of(donacion));

    List<Entrega> entregas = coordinador.planificarEntregasPendientes();
    String idEntrega = entregas.get(0).getId();

    CallbackPlanificacionRequest request = new CallbackPlanificacionRequest(
        Map.of(),
        List.of(idEntrega),
        LocalDate.now().toString()
    );
    coordinador.procesarCallback(request);

    assertNull(entregaRepository.buscarPorId(idEntrega));
    verify(donacionesClient, never()).marcarDonacionListaParaEntregar(idEntrega);
  }

  @Test
  void ejecutarPlanificacionDiariaEnviaLotesAlClienteExterno() {
    Camion camion = new Camion("ZZ333ZZ", 10f, 2.5f, 1500f);
    camionRepository.guardar(camion);

    Beneficiario beneficiario = new Beneficiario("ben-3", "Comedor C", "Calle 3");
    DonacionEnTransito donacion = new DonacionEnTransito("don-3", "Fideos", beneficiario);
    when(donacionesClient.buscarDonacionesAsignadas()).thenReturn(List.of(donacion));

    coordinador.ejecutarPlanificacionDiaria();

    verify(clienteExterno).enviarLote(any(Lote.class), anyList(), eq(CALLBACK_URL_ESPERADA));
  }
}