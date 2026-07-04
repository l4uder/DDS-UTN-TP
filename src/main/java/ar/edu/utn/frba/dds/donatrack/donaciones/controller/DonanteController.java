package ar.edu.utn.frba.dds.donatrack.donaciones.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.DonanteMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.DonanteRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.DonanteResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.service.DonanteService;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;

public class DonanteController {

  private final DonanteService service;

  public DonanteController(DonanteService service) {
    this.service = service;
  }

  public void listar(Context ctx) {
    try {
      List<DonanteResponse> donantes = service.listar(ctx.queryParam("tipo")).stream()
          .map(DonanteMapper::aResponse)
          .toList();
      ctx.json(donantes);
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    }
  }

  public void obtener(Context ctx) {
    try {
      Donante donante = service.obtener(ctx.pathParam("id"));
      ctx.json(DonanteMapper.aResponse(donante));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void crear(Context ctx) {
    try {
      DonanteRequest request = ctx.bodyAsClass(DonanteRequest.class);
      Donante creado = service.crear(request);
      ctx.status(201).json(DonanteMapper.aResponse(creado));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    }
  }

  public void actualizar(Context ctx) {
    try {
      DonanteRequest request = ctx.bodyAsClass(DonanteRequest.class);
      Donante actualizado = service.actualizar(ctx.pathParam("id"), request);
      ctx.json(DonanteMapper.aResponse(actualizado));
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
      service.eliminar(ctx.pathParam("id"));
      ctx.status(204);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

}
/*
* public void crear(Context ctx){
*   try{
*     DonanteRequest request = ctx.bodyAsClass(DonanteRequest.class) //aca bodyAsClass te combierte json en parametros de la clase DonanteRequest, es como que te matchea los parametros en json con los parametros del constructor de la clase DonanteRequest
*     Donante donante = DonanteMapper.aDominio(request)
*     DonanteRepository guardado = guardarDonante
* }
* }
* */