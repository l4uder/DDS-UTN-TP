package ar.edu.utn.frba.dds.donatrack.logistica.web.controller;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.web.convers.EntregaMapper;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega.EntregaFotoRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega.EntregaNoRecibidaRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.DonacionesClient;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoEntregadaRequest;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoErrorEntregaRequest;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.javalin.http.Context;
import java.util.List;

public class EntregaController {
  private final EntregaRepository repoEntregas;
  private final DonacionesClient donacionesBridge;

  public EntregaController(EntregaRepository repository, DonacionesClient donacionesClient) {
    this.repoEntregas = repository;
    this.donacionesBridge = donacionesClient;
  }

  public void obtenerTodos(Context ctx) {
    List<Entrega> entregas = repoEntregas.buscarTodas();

    ctx.status(200).json(EntregaMapper.aDtoResumen(entregas));
  }

  public void obtener(Context ctx) {
    String idEntrega = ctx.pathParam("id");

    Entrega entrega = buscarEntregaPorId(idEntrega);
    ctx.status(200).json(EntregaMapper.aDto(entrega));
  }

  public void agregarFoto(Context ctx) {
    //Cosas que recibo por URL
    String idEntrega = ctx.pathParam("id");
    //Cosas que recibo por Body
    EntregaFotoRequest request = ctx.bodyAsClass(EntregaFotoRequest.class);
    String urlFoto = request.urlFoto();

    Entrega entrega = buscarEntregaPorId(idEntrega);
    entrega.agregarFotoRecepcion(urlFoto);
    repoEntregas.actualizar(entrega);
    ctx.status(200);
  }

  public void confirmarRecibida(Context ctx) {
    String idEntrega = ctx.pathParam("id");

    Entrega entrega = buscarEntregaPorId(idEntrega);
    entrega.confirmarRecepcion();
    comunicarAlasDonacionesSuRecepcion(entrega.getDonaciones(), entrega.getCamionAsignado());
    repoEntregas.actualizar(entrega);
    ctx.status(200);
  }

  public void confirmarNoRecibida(Context ctx) {
    //Cosas que recibo por URL
    String idEntrega = ctx.pathParam("id");
    //Cosas que recibo por Body
    EntregaNoRecibidaRequest request = ctx.bodyAsClass(EntregaNoRecibidaRequest.class);
    String motivo = request.motivo();

    Entrega entrega = buscarEntregaPorId(idEntrega);
    entrega.marcarNoRecibida(motivo);
    comunicarAlasDonacionesErrorRecepcion(entrega.getDonaciones(), motivo);
    repoEntregas.actualizar(entrega);
    ctx.status(200);
  }

  public void reingresarADeposito(Context ctx) {
    String idEntrega = ctx.pathParam("id");

    Entrega entrega = buscarEntregaPorId(idEntrega);
    entrega.reingresarDeposito();
    comunicarAlasDonacionesReingresoAdeposito(entrega.getDonaciones());
    repoEntregas.eliminar(entrega);
    ctx.status(200);
  }

  //================== FUNCIONES AUXILIARES =====================
  private Entrega buscarEntregaPorId(String id) {
    Entrega entrega = repoEntregas.buscarPorId(id);
    if (entrega == null) throw new RecursoNoEncontradoException("Entrega no encontrada: " + id);

    return entrega;
  }

  private void comunicarAlasDonacionesSuRecepcion(List<DonacionEnTransito> donaciones, Camion camion) {
    donaciones.forEach(d ->
        donacionesBridge.cambiarEstadoDonacion(d.getId(), new CambioEstadoEntregadaRequest(camion.getPatente()))
    );
  }

  private void comunicarAlasDonacionesErrorRecepcion(List<DonacionEnTransito> donaciones, String motivo) {
    donaciones.forEach(d ->
        donacionesBridge.cambiarEstadoDonacion(d.getId(),new CambioEstadoErrorEntregaRequest(motivo))
    );
  }

  private void comunicarAlasDonacionesReingresoAdeposito(List<DonacionEnTransito> donaciones) {
    donaciones.forEach(d ->
        donacionesBridge.cambiarEstadoDonacionVueltaDeposito(d.getId())
    );
  }

}