package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.Ranking;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion.RankingMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.DonacionMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.service.AsignacionService;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;
import java.util.Map;

public class AsignacionController {

  private final AsignacionService service;

  public AsignacionController(AsignacionService service) {
    this.service = service;
  }

  public void ejecutarMatchmaking(Context ctx) {
    try {
      service.ejecutarMatchmaking();
      ctx.status(200).json(Map.of( "mensaje", "Matchmaking ejecutado correctamente"));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    }
  }

  public void obtenerRankings(Context ctx) {
    try {
      List<Ranking> rankings = service.obtenerRankings();
      ctx.json(rankings.stream().map(r -> RankingMapper.aResponse(r)).toList());
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
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
      String idDonacion = ctx.pathParam("id");
      Map<String, String> body = ctx.bodyAsClass(Map.class);
      String idBeneficiario = body.get("beneficiarioId");

      Donacion donacion = service.confirmarAsignacion(idDonacion, idBeneficiario);
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
