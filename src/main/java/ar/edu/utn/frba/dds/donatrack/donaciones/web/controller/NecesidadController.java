package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad.NecesidadMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad.NecesidadRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad.NecesidadResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.service.NecesidadService;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;

public class NecesidadController {

  private final NecesidadService service;

  public NecesidadController(NecesidadService service) {
    this.service = service;
  }

  public void listar(Context ctx) {
    try {
      List<NecesidadResponse> necesidades = service.listar(ctx.pathParam("id")).stream()
          .map(NecesidadMapper::aResponse)
          .toList();
      ctx.json(necesidades);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void obtener(Context ctx) {
    try {
      Necesidad necesidad = service.obtener(ctx.pathParam("id"), ctx.pathParam("nid"));
      ctx.json(NecesidadMapper.aResponse(necesidad));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void crear(Context ctx) {
    try {
      NecesidadRequest request = ctx.bodyAsClass(NecesidadRequest.class);
      Necesidad necesidad = NecesidadMapper.aDominio(request);
      Necesidad creada = service.crear(ctx.pathParam("id"), necesidad);
      ctx.status(201).json(NecesidadMapper.aResponse(creada));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void actualizar(Context ctx) {
    try {
      NecesidadRequest request = ctx.bodyAsClass(NecesidadRequest.class);
      Necesidad necesidad = service.obtener(ctx.pathParam("id"), ctx.pathParam("nid"));
      NecesidadMapper.actualizarDominio(necesidad, request);
      service.guardar(ctx.pathParam("id"));
      ctx.json(NecesidadMapper.aResponse(necesidad));
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
      service.eliminar(ctx.pathParam("id"), ctx.pathParam("nid"));
      ctx.status(204);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

}
