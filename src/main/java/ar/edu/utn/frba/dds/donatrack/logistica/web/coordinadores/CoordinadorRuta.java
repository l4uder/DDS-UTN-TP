package ar.edu.utn.frba.dds.donatrack.logistica.web.coordinadores;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion.Lote;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.planificadorexterno.ClientePlanificadorExterno;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.planificacion.CallbackPlanificacionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.microserviciosdonaciones.ConectorDonacionesApi;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.RutaRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CoordinadorRuta {
  private static final String CALLBACK_URL = "http://localhost:7071/planificaciones/callback-externo";

  private final RutaRepository rutaRepository;
  private final CamionRepository camionRepository;
  private final EntregaRepository entregaRepository;
  private final ConectorDonacionesApi donacionesClient;
  private final ClientePlanificadorExterno clienteExterno;

  public CoordinadorRuta(RutaRepository rutaRepository, CamionRepository camionRepository,
                         EntregaRepository entregaRepository, ConectorDonacionesApi donacionesClient,
                         ClientePlanificadorExterno clienteExterno){
    this.rutaRepository = rutaRepository;
    this.camionRepository = camionRepository;
    this.entregaRepository = entregaRepository;
    this.donacionesClient = donacionesClient;
    this.clienteExterno = clienteExterno;
  }

  public List<Entrega> planificarEntregasPendientes() {
    List<DonacionEnTransito> donacionesAsignadas = donacionesClient.buscarDonacionesAsignadas();
    List<Entrega> entregas = armarEntregasPendientes(donacionesAsignadas);
    entregas.forEach(entregaRepository::guardar);
    return entregas;
  }

  public void ejecutarPlanificacionDiaria() {
    List<Entrega> entregas = planificarEntregasPendientes();
    List<Camion> camiones = camionRepository.buscarTodos();
    List<Lote> lotes = Lote.armarLotes(entregas);

    lotes.forEach(lote -> clienteExterno.enviarLote(lote, camiones, CALLBACK_URL));
  }

  public List<Ruta> procesarCallback(CallbackPlanificacionRequest request) {
    LocalDate fecha = LocalDate.parse(request.fecha());
    Map<Camion, List<Entrega>> entregasPorCamion = obtenerEntregasPorCamion(request.entregasPorPatente());
    List<Entrega> entregasSinAsignar = request.entregasSinAsignar()==null? List.of() : request.entregasSinAsignar().stream().map(this::buscarEntregaPorId).toList();

    List<Ruta> rutas = crearRutas(fecha, entregasPorCamion);
    // Elimino las entregas que no fueron asignadas ya que las donaciones asociadas permanecen en
    // ASIGNACION_REALIZADA del lado de donaciones, nunca les aviso ningun cambio de estado, asu que
    // cuando se vuelva a consultar las donaciones asignadas la volveran a encuar para que entren
    // en el próximo ciclo de planificación.
    entregasSinAsignar.forEach(entregaRepository::eliminar);

    rutas.forEach(rutaRepository::guardar);
    rutas.forEach(ruta -> ruta.getEntregasOrdenadas().forEach(e ->
        propagarEstadoDonaciones(e, donacionesClient::marcarDonacionListaParaEntregar))
    );

    return rutas;
  }

  //======================= FUNCIONES AUXILIARES =======================
  //-- Entregas --
  private List<Entrega> armarEntregasPendientes(List<DonacionEnTransito> donacionesAsignadas) {
    Map<Beneficiario, List<DonacionEnTransito>> agrupadas = donacionesAsignadas.stream()
        .collect(Collectors.groupingBy(DonacionEnTransito::getBeneficiario));

    List<Entrega> entregas = new ArrayList<>();
    agrupadas.forEach((beneficiario, donaciones) ->
        entregas.add(new Entrega(beneficiario, donaciones, null)));

    return entregas;
  }

  //--Helper--
  private void propagarEstadoDonaciones(Entrega entrega, Consumer<String> command) {
    entrega.getDonaciones().forEach(d -> command.accept(d.getId()));
  }

  private Map<Camion, List<Entrega>> obtenerEntregasPorCamion(Map<String, List<String>> entregasPorPatente) {
    Map<Camion, List<Entrega>> entregasPorCamion = new HashMap<>();

    entregasPorPatente.forEach((patente, idsEntregas) -> {
      Camion camion = buscarCamionPorPatente(patente);
      List<Entrega> entregas = idsEntregas.stream()
          .map(this::buscarEntregaPorId)
          .toList();
      entregasPorCamion.put(camion, entregas);
    });

    return entregasPorCamion;
  }

  private List<Ruta> crearRutas(LocalDate fecha, Map<Camion, List<Entrega>> entregasPorCamion) {
    List<Ruta> rutasCreadas = new ArrayList<>();

    entregasPorCamion.forEach((camion, entregasOrdenadas) -> {
      entregasOrdenadas.forEach(entrega -> {
        entrega.reasignarCamion(camion);
        entrega.confirmarListaParaEntregar();
      });
      rutasCreadas.add(new Ruta(camion, fecha, entregasOrdenadas));
    });

    return rutasCreadas;
  }

  private Camion buscarCamionPorPatente(String patente) {
    Camion camion = camionRepository.buscarPorPatente(patente);
    if (camion == null) throw new RecursoNoEncontradoException("Camión no encontrado: " + patente);
    return camion;
  }

  private Entrega buscarEntregaPorId(String id) {
    Entrega entrega = entregaRepository.buscarPorId(id);
    if (entrega == null) throw new RecursoNoEncontradoException("Entrega no encontrado: " + id);
    return entrega;
  }

}