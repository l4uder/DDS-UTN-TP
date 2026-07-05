package ar.edu.utn.frba.dds.donatrack.donaciones.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion.DonacionMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion.DonacionRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion.DonacionResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.service.DonacionService;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;

public class DonacionController {

  private final DonacionService service;

  public DonacionController(DonacionService service) {
    this.service = service;
  }

  public void listar(Context ctx) {
    try {
      List<DonacionResponse> donaciones = service.listar(ctx.queryParam("estado")).stream()
          .map(DonacionMapper::aResponse)
          .toList();
      ctx.json(donaciones);
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    }
  }

  public void obtener(Context ctx) {
    try {
      Donacion donacion = service.obtener(ctx.pathParam("id"));
      ctx.json(DonacionMapper.aResponse(donacion));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void crear(Context ctx) {
    try {
      DonacionRequest request = ctx.bodyAsClass(DonacionRequest.class);
      Donacion creada = service.crear(request);
      ctx.status(201).json(DonacionMapper.aResponse(creada));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    }
  }

  public void actualizar(Context ctx) {
    try {
      DonacionRequest request = ctx.bodyAsClass(DonacionRequest.class);
      Donacion actualizada = service.actualizar(ctx.pathParam("id"), request);
      ctx.json(DonacionMapper.aResponse(actualizada));
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

  public void eliminar(Context ctx) {
    try {
      service.eliminar(ctx.pathParam("id"));
      ctx.status(204);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

}
