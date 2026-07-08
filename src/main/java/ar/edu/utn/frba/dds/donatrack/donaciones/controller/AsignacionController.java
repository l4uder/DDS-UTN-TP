package ar.edu.utn.frba.dds.donatrack.donaciones.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador.Ranking;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.asignacion.AsignacionRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.asignacion.RankingMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion.DonacionMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.service.AsignacionService;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;

public class AsignacionController {

  private final AsignacionService service;

  public AsignacionController(AsignacionService service) {
    this.service = service;
  }

  public void ejecutarMatchmaking(Context ctx) {
    try {
      Ranking ranking = service.ejecutarMatchmaking(ctx.pathParam("id"));
      ctx.json(RankingMapper.aResponse(ranking));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    }
  }

  public void obtenerRanking(Context ctx) {
    try {
      Ranking ranking = service.obtenerRanking(ctx.pathParam("id"));
      ctx.json(RankingMapper.aResponse(ranking));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void confirmarAsignacion(Context ctx) {
    try {
      AsignacionRequest request = ctx.bodyAsClass(AsignacionRequest.class);
      Donacion donacion = service.confirmarAsignacion(ctx.pathParam("id"), request);
      ctx.json(DonacionMapper.aResponse(donacion));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    }
  }

}
