package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
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
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.javalin.http.Context;
import java.util.List;
import java.util.Map;

public class AsignacionController {
  private final DonacionRepository repoDonaciones;
  private final BeneficiarioRepository repoBeneficiarios;
  private final RankingRepository repoRankings;

  public AsignacionController(DonacionRepository repoDonaciones, BeneficiarioRepository repoBeneficiarios,
                              RankingRepository repoRankings) {
    this.repoDonaciones = repoDonaciones;
    this.repoBeneficiarios = repoBeneficiarios;
    this.repoRankings = repoRankings;
  }

  public void crearRankings(Context ctx) {
      GeneradorRankings generadorRankings = new GeneradorRankings(repoRankings);
      generadorRankings.agregarAlgoritmo(new CompatibilidadSemantica());
      generadorRankings.agregarAlgoritmo(new PrioridadSubAtendidos());

      List<Beneficiario> beneficiarios = repoBeneficiarios.buscarTodos();
      List<Donacion> donaciones = repoDonaciones.buscarTodoPorEstado(TipoEstadoDonacion.EN_DEPOSITO);

      generadorRankings.asignar(donaciones, beneficiarios);
      ctx.status(200).json(Map.of( "mensaje", "Matchmaking ejecutado correctamente"));
  }

  public void obtenerTodos(Context ctx) {
      List<Ranking> rankings = repoRankings.buscarTodos();
      ctx.status(200).json(rankings.stream().map(RankingMapper::aDto).toList());
  }

  public void obtener(Context ctx) {
      //Cosas que recibo por URL --> Path param
      String idRanking = ctx.pathParam("id");

      Ranking ranking = buscarRankingPorId(idRanking);

      ctx.status(200).json(RankingMapper.aDto(ranking));
  }

  public void confirmar(Context ctx) {
      //Cosas que recibo por URL --> Path param
      String idRanking = ctx.pathParam("id");
      //Cosas que recibo por Body
      ConfirmacionBody body = ctx.bodyAsClass(ConfirmacionBody.class);
      String idBeneficiario = body.beneficiarioId();

      Ranking ranking = buscarRankingPorId(idRanking);
      Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);

      ranking.confirmada();
      Donacion donacion = ranking.getDonacion();
      donacion.confirmarAsignacion(beneficiario);
      repoDonaciones.actualizar(donacion);
      repoBeneficiarios.actualizar(beneficiario);
      repoRankings.actualizar(ranking);
      ctx.status(200).json(DonacionMapper.aDto(donacion));
  }

  //=================== FUNCIONES AUXILIARES ========================
  private Beneficiario buscarBeneficiarioPorId(String id) {
    Beneficiario beneficiario = repoBeneficiarios.buscarPorId(id);
    if (beneficiario == null) throw new RecursoNoEncontradoException("No existe el beneficiario: " + id);
    return beneficiario;
  }

  private Ranking buscarRankingPorId(String id) {
    Ranking ranking = repoRankings.buscarPorId(id);
    if (ranking == null) throw new RecursoNoEncontradoException("No existe el ranking: " + id);
    return ranking;
  }

}
