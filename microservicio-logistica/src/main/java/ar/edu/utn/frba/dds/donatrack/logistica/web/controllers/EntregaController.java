package ar.edu.utn.frba.dds.donatrack.logistica.web.controllers;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.web.convers.EntregaMapper;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega.EntregaFotoRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.entrega.EntregaNoRecibidaRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.microserviciosdonaciones.ConectorDonacionesApi;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BodyException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.javalin.http.Context;
import java.util.List;

public class EntregaController {
  private final EntregaRepository repoEntregas;
  private final ConectorDonacionesApi donacionesBridge;

  public EntregaController(EntregaRepository repository, ConectorDonacionesApi donacionesClient) {
    this.repoEntregas = repository;
    this.donacionesBridge = donacionesClient;
  }

  public void obtenerTodos(Context ctx) {
    List<Entrega> entregas = repoEntregas.buscarTodas();

    ctx.status(200).json(EntregaMapper.aDtoResumen(entregas));
  }

  public void obtener(Context ctx) {
    Long id = Long.valueOf(ctx.pathParam("id"));

    Entrega entrega = buscarEntregaPorId(id);
    ctx.status(200).json(EntregaMapper.aDto(entrega));
  }

  public void agregarFoto(Context ctx) {
    //Cosas que recibo por URL
    Long id = Long.valueOf(ctx.pathParam("id"));
    //Cosas que recibo por Body
    EntregaFotoRequest request = ctx.bodyAsClass(EntregaFotoRequest.class);
    String urlFoto = request.urlFoto();

    Entrega entrega = buscarEntregaPorId(id);
    entrega.agregarFotoRecepcion(urlFoto);
    repoEntregas.actualizar(entrega);
    ctx.status(200);
  }

  public void confirmarRecibida(Context ctx) {
    Long id = Long.valueOf(ctx.pathParam("id"));

    Entrega entrega = buscarEntregaPorId(id);
    entrega.confirmarRecepcion();
    repoEntregas.actualizar(entrega);
    comunicarAlasDonacionesSuRecepcion(entrega.getDonaciones(), "https://..../comprobantes/...");
    ctx.status(200);
  }

  public void confirmarNoRecibida(Context ctx) {
    //Cosas que recibo por URL
    Long id = Long.valueOf(ctx.pathParam("id"));
    //Cosas que recibo por Body
    EntregaNoRecibidaRequest request = ctx.bodyAsClass(EntregaNoRecibidaRequest.class);
    if (request.motivo()==null) throw new BodyException("Bad Request, necesita: 'motivo' ");
    String motivo = request.motivo();

    Entrega entrega = buscarEntregaPorId(id);
    entrega.marcarNoRecibida(motivo);
    repoEntregas.actualizar(entrega);
    comunicarAlasDonacionesErrorRecepcion(entrega.getDonaciones(), motivo);
    ctx.status(200);
  }

  public void reingresarADeposito(Context ctx) {
    Long id = Long.valueOf(ctx.pathParam("id"));

    Entrega entrega = buscarEntregaPorId(id);
    entrega.reingresarDeposito();
    repoEntregas.eliminar(entrega.getId());
    comunicarAlasDonacionesReingresoAdeposito(entrega.getDonaciones());
    ctx.status(200);
  }
  //================== FUNCIONES AUXILIARES =====================
  private Entrega buscarEntregaPorId(Long id) {
    Entrega entrega = repoEntregas.buscarPorId(id);
    if (entrega == null) throw new RecursoNoEncontradoException("Entrega no encontrada: " + id);

    return entrega;
  }

  private void comunicarAlasDonacionesSuRecepcion(List<DonacionEnTransito> donaciones, String linkComprobante) {
    donaciones.forEach(d ->
        donacionesBridge.marcarDonacionEntregaExitosa(d.getId(), linkComprobante)
    );
  }

  private void comunicarAlasDonacionesErrorRecepcion(List<DonacionEnTransito> donaciones, String motivo) {
    donaciones.forEach(d ->
        donacionesBridge.marcarDonacionErrorEntrega(d.getId(), motivo)
    );
  }

  private void comunicarAlasDonacionesReingresoAdeposito(List<DonacionEnTransito> donaciones) {
    donaciones.forEach(d ->
        donacionesBridge.marcarDonacionVueltaDeposito(d.getId())
    );
  }

}