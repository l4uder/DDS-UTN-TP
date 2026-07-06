package ar.edu.utn.frba.dds.donatrack.donaciones.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.beneficiario.BeneficiarioMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.beneficiario.BeneficiarioRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.beneficiario.BeneficiarioResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.service.BeneficiarioService;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;

public class BeneficiarioController {

  private final BeneficiarioService service;

  public BeneficiarioController(BeneficiarioService service) {
    this.service = service;
  }

  public void listar(Context ctx) {
    List<BeneficiarioResponse> beneficiarios = service.listar().stream()
        .map(BeneficiarioMapper::aResponse)
        .toList();
    ctx.json(beneficiarios);
  }

  public void obtener(Context ctx) {
    try {
      Beneficiario beneficiario = service.obtener(ctx.pathParam("id"));
      ctx.json(BeneficiarioMapper.aResponse(beneficiario));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void crear(Context ctx) {
    try {
      BeneficiarioRequest request = ctx.bodyAsClass(BeneficiarioRequest.class);
      Beneficiario creado = service.crear(request);
      ctx.status(201).json(BeneficiarioMapper.aResponse(creado));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    }
  }

  public void actualizar(Context ctx) {
    try {
      BeneficiarioRequest request = ctx.bodyAsClass(BeneficiarioRequest.class);
      Beneficiario actualizado = service.actualizar(ctx.pathParam("id"), request);
      ctx.json(BeneficiarioMapper.aResponse(actualizado));
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
