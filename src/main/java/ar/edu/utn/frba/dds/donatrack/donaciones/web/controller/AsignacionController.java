package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.EstadoRanking;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.GeneradorRankings;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.Ranking;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.algoritmos.CompatibilidadSemantica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.algoritmos.PrioridadSubAtendidos;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.RankingRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.RankingMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.DonacionMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion.ConfirmacionBody;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;
import java.util.Map;

public class AsignacionController {
  private final DonacionRepository repoDonaciones;
  private final BeneficiarioRepository repoBeneficiarios;
  private final RankingRepository repoRankings;

  public AsignacionController(/*DonacionRepository repoDonaciones, BeneficiarioRepository repoBeneficiarios, RankingRepository repoRankings*/) {
    this.repoDonaciones = DonacionRepository.getInstancia();//= repoDonaciones;
    this.repoBeneficiarios = BeneficiarioRepository.getInstancia();//= repoBeneficiarios;
    this.repoRankings = RankingRepository.getInstancia();//= repoRankings;
  }

  public void crearRankings(Context ctx) {
    try {
      GeneradorRankings generadorRankings = new GeneradorRankings(repoRankings);
      generadorRankings.agregarAlgoritmo(new CompatibilidadSemantica());
      generadorRankings.agregarAlgoritmo(new PrioridadSubAtendidos());

      List<Beneficiario> beneficiarios = repoBeneficiarios.buscarTodos();
      List<Donacion> donaciones = repoDonaciones.buscarTodoPorEstado(TipoEstadoDonacion.EN_DEPOSITO);

      generadorRankings.asignar(donaciones, beneficiarios);

      ctx.status(200).json(Map.of( "mensaje", "Matchmaking ejecutado correctamente"));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void obtenerTodos(Context ctx) {
    try {
      List<Ranking> rankings = repoRankings.buscarTodos();
      ctx.status(200).json(rankings.stream().map(RankingMapper::aDto).toList());
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void obtener(Context ctx) {
    try {
      String idRanking = ctx.pathParam("id");
      Ranking ranking = repoRankings.buscarPorId(idRanking);
      if (ranking == null) throw new RecursoNoEncontradoException("El ranking " + idRanking + " no existe");
      ctx.json(RankingMapper.aDto(ranking));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void confirmar(Context ctx) {
    try {
      String idRanking = ctx.pathParam("id");
      ConfirmacionBody body = ctx.bodyAsClass(ConfirmacionBody.class);
      String stringEstado = body.getEstado();
      String idBeneficiario = body.getBeneficiarioId();

      Ranking ranking = repoRankings.buscarPorId(idRanking);
      if (ranking == null) throw new RecursoNoEncontradoException("El ranking " + idRanking + " no existe");

      Beneficiario beneficiario = repoBeneficiarios.buscarPorId(idBeneficiario);
      if (beneficiario == null) throw new RecursoNoEncontradoException("No existe beneficiario con id: " + idBeneficiario);

      EstadoRanking estado = aEstadoRanking(stringEstado);
      if (estado == null) throw new DomainValidationException("No existe el estado de ranking: " + stringEstado);

      ranking.setEstado(estado);
      Donacion donacion = ranking.getDonacion();
      donacion.confirmarAsignacion(beneficiario);

      repoDonaciones.actualizar(donacion);
      repoBeneficiarios.actualizar(beneficiario);
      repoRankings.actualizar(ranking);

      ctx.status(200).json(DonacionMapper.aDto(donacion));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  private EstadoRanking aEstadoRanking(String estado) {
    try {
      return EstadoRanking.valueOf(estado.toUpperCase());
    } catch (IllegalArgumentException | NullPointerException f) {
      return null;
    }
  }
}
