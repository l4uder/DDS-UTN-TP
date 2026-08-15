package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion.Lote;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.planificadorexterno.ClientePlanificadorExterno;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.web.coordinadores.CoordinadorRuta;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.RutaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.microserviciosdonaciones.ConectorDonacionesApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;

public class CoordinadorRutaTest {

  private Beneficiario beneficiarioA;
  private Beneficiario beneficiarioB;
  private List<Camion> camiones;

  private CamionRepository camionRepository;
  private EntregaRepository entregaRepository;
  private RutaRepository rutaRepository;
  private ConectorDonacionesApi donacionesClient;
  private ClientePlanificadorExterno clienteExterno;
  private CoordinadorRuta coordinador;

  @BeforeEach
  void setUp() {
    beneficiarioA = new Beneficiario("ben-a", "Comedor A", "Av. BeneficiarioA");
    beneficiarioB = new Beneficiario("ben-b", "Comedor B", "Av. BeneficiarioB");
    camiones = List.of(new Camion("AB123CD", 10f, 2.5f, 1500f));

    camionRepository = mock(CamionRepository.class);
    entregaRepository = mock(EntregaRepository.class);
    rutaRepository = mock(RutaRepository.class);
    donacionesClient = mock(ConectorDonacionesApi.class);
    clienteExterno = mock(ClientePlanificadorExterno.class);

    when(camionRepository.buscarTodos()).thenReturn(camiones);

    coordinador = new CoordinadorRuta(
        rutaRepository, camionRepository, entregaRepository, donacionesClient, clienteExterno);
  }

  private DonacionEnTransito donacionPara(Beneficiario beneficiario, int nro) {
    return new DonacionEnTransito("don-" + beneficiario.getId() + "-" + nro, "Arroz", beneficiario);
  }

  @Test
  void agrupaDonacionesPorDestinoEnEntregas() {
    List<DonacionEnTransito> donaciones = List.of(
        donacionPara(beneficiarioA, 1),
        donacionPara(beneficiarioA, 2),
        donacionPara(beneficiarioB, 1)
    );
    when(donacionesClient.buscarDonacionesAsignadas()).thenReturn(donaciones);

    List<Entrega> entregas = coordinador.planificarEntregasPendientes();

    assertEquals(2, entregas.size());
    assertTrue(entregas.stream().anyMatch(e ->
        e.getDestino().equals(beneficiarioA) && e.getDonaciones().size() == 2));
    assertTrue(entregas.stream().anyMatch(e ->
        e.getDestino().equals(beneficiarioB) && e.getDonaciones().size() == 1));

    verify(entregaRepository, times(2)).guardar(any(Entrega.class));
  }

  @Test
  void armaLotesRespetandoElLimiteDeCienDonaciones() {
    // 150 beneficiarios distintos, 1 donación cada uno -> 150 Entrega de 1 donación,
    // así el loteo tiene margen real para repartir en más de un lote de a 100.
    List<DonacionEnTransito> donaciones = new ArrayList<>();
    for (int i = 0; i < 150; i++) {
      Beneficiario beneficiario = new Beneficiario("ben-" + i, "Comedor " + i, "Calle " + i);
      donaciones.add(new DonacionEnTransito("don-" + i, "Arroz", beneficiario));
    }
    when(donacionesClient.buscarDonacionesAsignadas()).thenReturn(donaciones);

    coordinador.ejecutarPlanificacionDiaria();

    // 150 entregas de 1 donación -> deberían repartirse en exactamente 2 lotes (100 + 50)
    verify(clienteExterno, times(2)).enviarLote(any(Lote.class), eq(camiones), anyString());
  }

  @Test
  void unaSolaEntregaConMasDeCienDonacionesQuedaEnUnUnicoLote() {
    // Documenta el caso límite: si un mismo beneficiario acumula más de 100 donaciones,
    // quedan todas en UNA Entrega, y el loteo no puede partirla — el límite de 100
    // se respeta a nivel de cantidad de Entregas agrupadas por lote, no a nivel de
    // donación individual cuando ya vienen preagrupadas por beneficiario.
    List<DonacionEnTransito> donaciones = new ArrayList<>();
    for (int i = 0; i < 150; i++) {
      donaciones.add(donacionPara(beneficiarioA, i));
    }
    when(donacionesClient.buscarDonacionesAsignadas()).thenReturn(donaciones);

    coordinador.ejecutarPlanificacionDiaria();

    ArgumentCaptor<Lote> loteCaptor = ArgumentCaptor.forClass(Lote.class);
    verify(clienteExterno, times(1)).enviarLote(loteCaptor.capture(), eq(camiones), anyString());
    assertEquals(1, loteCaptor.getValue().getEntregas().size());
    assertEquals(150, loteCaptor.getValue().cantidadDonaciones());
  }
}