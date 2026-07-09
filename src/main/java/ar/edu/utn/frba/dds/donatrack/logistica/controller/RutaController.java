package ar.edu.utn.frba.dds.donatrack.logistica.controller;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Chofer;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.planificacion.CallbackPlanificacionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.ruta.RutaMapper;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.ruta.RutaResponse;
import ar.edu.utn.frba.dds.donatrack.logistica.service.GestorRuta;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.javalin.http.Context;

import java.util.List;

public class RutaController {
  private final GestorRuta gestor;

  public RutaController(GestorRuta gestor) {
    this.gestor = gestor;
  }

  public void listar(Context ctx) {
    List<RutaResponse> rutas = gestor.listar().stream()
        .map(RutaMapper::aResponse)
        .toList();
    ctx.json(rutas);
  }

  public void obtener(Context ctx) {
    try {
      ctx.json(RutaMapper.aResponse(gestor.obtener(ctx.pathParam("id"))));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void asignarChofer(Context ctx) {
    try {
      Chofer chofer = ctx.bodyAsClass(Chofer.class);
      gestor.asignarChofer(ctx.pathParam("id"), chofer);
      ctx.status(200);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (IllegalStateException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    }
  }

  public void iniciarRecorrido(Context ctx) {
    try {
      gestor.iniciarRecorrido(ctx.pathParam("id"));
      ctx.status(200);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (IllegalStateException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    }
  }

  public void recibirCallback(Context ctx) {
    try {
      CallbackPlanificacionRequest request =
          ctx.bodyAsClass(CallbackPlanificacionRequest.class);
      List<Ruta> rutas = gestor.procesarCallback(request);
      ctx.status(201).json(rutas.stream()
          .map(RutaMapper::aResponse)
          .toList());
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }
}