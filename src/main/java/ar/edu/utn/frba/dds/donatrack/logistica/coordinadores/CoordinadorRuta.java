package ar.edu.utn.frba.dds.donatrack.logistica.coordinadores;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Chofer;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion.PlanificadorLogistico;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion.ResultadoPlanificacion;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.DonacionAsignadaDTO;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.planificacion.CallbackPlanificacionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.integracion.DonacionesClient;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.RutaRepository;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoInicioRutaRequest;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CoordinadorRuta {

  private final RutaRepository rutaRepository;
  private final CamionRepository camionRepository;
  private final EntregaRepository entregaRepository;
  private final DonacionesClient donacionesClient;
  private final PlanificadorLogistico planificador;

  public CoordinadorRuta(RutaRepository rutaRepository, CamionRepository camionRepository,
                         EntregaRepository entregaRepository, DonacionesClient donacionesClient,
                         PlanificadorLogistico planificador) {
    this.rutaRepository = rutaRepository;
    this.camionRepository = camionRepository;
    this.entregaRepository = entregaRepository;
    this.donacionesClient = donacionesClient;
    this.planificador = planificador;
  }

  public List<Entrega> planificarEntregasPendientes() {
    List<DonacionAsignadaDTO> donacionesAsignadas = donacionesClient.buscarDonacionesAsignadas();
    List<Entrega> entregas = planificador.armarEntregasPendientes(donacionesAsignadas);
    entregas.forEach(entregaRepository::guardar);
    return entregas;
  }

  public List<Ruta> procesarCallback(CallbackPlanificacionRequest request) {
    LocalDate fecha = LocalDate.parse(request.fecha());

    Map<Camion, List<Entrega>> entregasPorCamion = new HashMap<>();
    request.entregasPorPatente().forEach((patente, idsEntregas) -> {
      Camion camion = camionRepository.buscarPorPatente(patente)
          .orElseThrow(() -> new RecursoNoEncontradoException("Camión no encontrado: " + patente));
      List<Entrega> entregas = idsEntregas.stream().map(entregaRepository::buscarPorId).toList();
      entregasPorCamion.put(camion, entregas);
    });

    List<Entrega> sinAsignar = request.entregasSinAsignar().stream()
        .map(entregaRepository::buscarPorId)
        .toList();

    // Elimino las entregas que no fueron asignadas ya que las donaciones asociadas permanecen en
    // ASIGNACION_REALIZADA del lado de donaciones, nunca les aviso ningun cambio de estado, asu que
    // cuando se vuelva a consultar las donaciones asignadas la volveran a encuar para que entren
    // en el próximo ciclo de planificación.
    sinAsignar.forEach(e -> entregaRepository.eliminar(e.getId()));

    ResultadoPlanificacion resultado = new ResultadoPlanificacion(entregasPorCamion, sinAsignar);
    List<Ruta> rutas = planificador.procesarResultadoPlanificacion(resultado, fecha);

    rutas.forEach(rutaRepository::guardar);
    rutas.forEach(ruta -> ruta.getEntregasOrdenadas().forEach(e ->
        propagarEstadoDonaciones(e, donacionesClient::cambiarEstadoDonacionLista)));

    return rutas;
  }

  public void iniciarRecorrido(String id) {
    Ruta ruta = rutaRepository.buscarPorId(id);
    ruta.iniciarRecorrido();
    rutaRepository.guardar(ruta);

    String linkMapa = ruta.getCamion().getLinkSeguimiento();
    ruta.getEntregasOrdenadas().forEach(e ->
        propagarEstadoDonaciones(e, donacionId ->
            donacionesClient.cambiarEstadoDonacion(donacionId, new CambioEstadoInicioRutaRequest(linkMapa))));
  }

  public void asignarChofer(String id, Chofer chofer) {
    Ruta ruta = rutaRepository.buscarPorId(id);
    ruta.asignarChofer(chofer);
    rutaRepository.guardar(ruta);
  }

  private void propagarEstadoDonaciones(Entrega entrega, Consumer<String> command) {
    entrega.getDonaciones().forEach(d -> command.accept(d.getId()));
  }

  public void ejecutarPlanificacionDiaria() {
    List<Entrega> entregas = planificarEntregasPendientes(); // ya arma y guarda
    List<List<Entrega>> lotes = planificador.armarLotesEntrega(entregas);

    lotes.forEach(lote -> {
      System.out.println("Enviando lote de " + lote.size() + " entregas al planificador externo");
      // Falta la llamada HTTP real al componente externo de planificación (no implementado
      // en este TP — el callback en /rutas/callback-planificacion simula respuesta).
    });
  }
}