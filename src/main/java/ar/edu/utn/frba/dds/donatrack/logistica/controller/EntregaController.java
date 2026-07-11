package ar.edu.utn.frba.dds.donatrack.logistica.controller;

import ar.edu.utn.frba.dds.donatrack.logistica.dto.entrega.EntregaFotoRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.entrega.EntregaMapper;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.entrega.EntregaNoRecibidaRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.entrega.EntregaResponse;
import ar.edu.utn.frba.dds.donatrack.logistica.service.GestorEntrega;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.javalin.http.Context;

import java.util.List;

public class EntregaController {
  private final GestorEntrega gestor;

  public EntregaController(GestorEntrega gestor) {
    this.gestor = gestor;
  }

  public void listar(Context ctx) {
    List<EntregaResponse> entregas = gestor.listar().stream()
        .map(EntregaMapper::aResponse)
        .toList();
    ctx.json(entregas);
  }

  public void obtener(Context ctx) {
    try {
      ctx.json(EntregaMapper.aResponse(gestor.obtener(ctx.pathParam("id"))));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void confirmarRecepcion(Context ctx) {
    try {
      gestor.confirmarRecepcion(ctx.pathParam("id"));
      ctx.status(200);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    }
  }

  public void marcarNoRecibida(Context ctx) {
    try {
      EntregaNoRecibidaRequest request = ctx.bodyAsClass(EntregaNoRecibidaRequest.class);
      gestor.marcarNoRecibida(ctx.pathParam("id"), request.motivo());
      ctx.status(200);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    }
  }

  public void reingresarADeposito(Context ctx) {
    try {
      gestor.reingresarADeposito(ctx.pathParam("id"));
      ctx.status(200);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (IllegalStateException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    }
  }

  public void agregarFoto(Context ctx) {
    try {
      EntregaFotoRequest request = ctx.bodyAsClass(EntregaFotoRequest.class);
      gestor.agregarFoto(ctx.pathParam("id"), request.urlFoto());
      ctx.status(200);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    }
  }
}