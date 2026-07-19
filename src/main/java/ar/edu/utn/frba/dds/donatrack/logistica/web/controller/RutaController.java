package ar.edu.utn.frba.dds.donatrack.logistica.web.controller;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.coordinadores.CoordinadorRuta;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.chofer.ChoferRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.planificacion.CallbackPlanificacionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.RutaRepository;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import io.javalin.http.Context;

import java.util.List;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import com.google.gson.JsonSyntaxException;

public class RutaController {

  private final RutaRepository repository;
  private final CoordinadorRuta coordinador;

  public RutaController(RutaRepository repository, CoordinadorRuta coordinador) {
    this.repository = repository;
    this.coordinador = coordinador;
  }

  public void listar(Context ctx) {
    ctx.json(repository.buscarTodas());
  }

  public void obtener(Context ctx) {
    ctx.json(repository.buscarPorId(ctx.pathParam("id")));
  }

  public void asignarChofer(Context ctx) {
    try {
      ChoferRequest request = ctx.bodyAsClass(ChoferRequest.class);
      coordinador.asignarChofer(ctx.pathParam("id"), request.aDominio());
      ctx.status(200);
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ExceptionHandlers.ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ExceptionHandlers.ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ExceptionHandlers.ErrorResponse(404, e.getMessage()));
    } catch (IllegalStateException e) {
      ctx.status(409).json(new ExceptionHandlers.ErrorResponse(409, e.getMessage()));
    }
  }

  public void planificar(Context ctx) {
    List<Entrega> entregas = coordinador.planificarEntregasPendientes();
    ctx.status(202).json(entregas);
  }

  public void callback(Context ctx) {
    try {
      CallbackPlanificacionRequest request = ctx.bodyAsClass(CallbackPlanificacionRequest.class);
      List<Ruta> rutas = coordinador.procesarCallback(request);
      ctx.status(201).json(rutas);
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ExceptionHandlers.ErrorResponse(400, "El body no es un JSON valido"));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ExceptionHandlers.ErrorResponse(404, e.getMessage()));
    }
  }

  public void iniciar(Context ctx) {
    try {
      coordinador.iniciarRecorrido(ctx.pathParam("id"));
      ctx.status(200);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ExceptionHandlers.ErrorResponse(404, e.getMessage()));
    } catch (IllegalStateException e) {
      ctx.status(409).json(new ExceptionHandlers.ErrorResponse(409, e.getMessage()));
    }
  }
}