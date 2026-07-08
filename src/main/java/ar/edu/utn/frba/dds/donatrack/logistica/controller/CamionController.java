package ar.edu.utn.frba.dds.donatrack.logistica.controller;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.camion.CamionMapper;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.camion.CamionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.camion.CamionResponse;
import ar.edu.utn.frba.dds.donatrack.logistica.service.CamionService;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;

public class CamionController {

  private final CamionService service;

  public CamionController(CamionService service) {
    this.service = service;
  }

  public void listar(Context ctx) {
    List<CamionResponse> camiones = service.listar().stream()
        .map(CamionMapper::aResponse)
        .toList();
    ctx.json(camiones);
  }

  public void obtener(Context ctx) {
    try {
      Camion camion = service.obtener(ctx.pathParam("patente"));
      ctx.json(CamionMapper.aResponse(camion));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void crear(Context ctx) {
    try {
      CamionRequest request = ctx.bodyAsClass(CamionRequest.class);
      Camion creado = service.crear(request);
      ctx.status(201).json(CamionMapper.aResponse(creado));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    }
  }

  public void actualizar(Context ctx) {
    try {
      CamionRequest request = ctx.bodyAsClass(CamionRequest.class);
      Camion actualizado = service.actualizar(ctx.pathParam("patente"), request);
      ctx.json(CamionMapper.aResponse(actualizado));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void eliminar(Context ctx) {
    try {
      service.eliminar(ctx.pathParam("patente"));
      ctx.status(204);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

}
