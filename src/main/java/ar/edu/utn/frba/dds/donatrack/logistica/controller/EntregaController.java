package ar.edu.utn.frba.dds.donatrack.logistica.controller;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.entrega.EntregaFotoRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.entrega.EntregaNoRecibidaRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.entrega.EntregaResponse;
import ar.edu.utn.frba.dds.donatrack.logistica.coordinadores.CoordinadorEntrega;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import io.javalin.http.Context;

public class EntregaController {
  private final EntregaRepository repository;
  private final CoordinadorEntrega coordinador;


  public EntregaController(
      EntregaRepository repository,
      CoordinadorEntrega coordinador
  ){
    this.repository = repository;
    this.coordinador = coordinador;
  }


  public void listar(Context ctx){
    ctx.json(
        repository.buscarTodas()
            .stream()
            .map(EntregaResponse::desde)
            .toList()
    );

  }

  public void obtener(Context ctx){
    Entrega entrega = repository.buscarPorId(ctx.pathParam("id"));

    ctx.json(EntregaResponse.desde(entrega));
  }


  public void confirmarRecepcion(Context ctx){
    coordinador.confirmarRecepcion(ctx.pathParam("id"));
    ctx.status(200);
  }

  public void marcarNoRecibida(Context ctx){
    EntregaNoRecibidaRequest request = ctx.bodyAsClass(EntregaNoRecibidaRequest.class);
    coordinador.marcarNoRecibida(ctx.pathParam("id"), request.motivo());
    ctx.status(200);
  }

  public void agregarFoto(Context ctx){
    Entrega entrega = repository.buscarPorId(ctx.pathParam("id"));

    EntregaFotoRequest request = ctx.bodyAsClass(EntregaFotoRequest.class);

    entrega.agregarFotoRecepcion(request.urlFoto());
    repository.guardar(entrega);
    ctx.status(200);
  }

  public void reingresarADeposito(Context ctx){
    coordinador.reingresarDeposito(ctx.pathParam("id"));
    ctx.status(200);
  }

}