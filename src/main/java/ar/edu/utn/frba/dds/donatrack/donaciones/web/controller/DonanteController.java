package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.DonanteMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.Arrays;
import java.util.List;

public class DonanteController implements WithSimplePersistenceUnit {
  private final DonanteRepository repoDonantes;

  public DonanteController(DonanteRepository repoDonantes) {
    this.repoDonantes = repoDonantes;
  }

  public void crear(Context ctx) {
    //Cosas que recibo por Body
    DonanteRequest donanteDto = ctx.bodyAsClass(DonanteRequest.class);

    Donante donante = DonanteMapper.aDominio(donanteDto);

    beginTransaction();
    repoDonantes.guardar(donante);
    commitTransaction();

    ctx.status(201).json("Donante creado correctamente");
  }

  public void obtenerTodos(Context ctx) {
    //Cosas que recibo por URL --> Query param
    String tipo = ctx.queryParam("tipo");

    beginTransaction();
    List<Donante> donantes = (tipo==null || tipo.isBlank()) ?
        repoDonantes.buscarTodos() :
        repoDonantes.buscarPorTipoPersona(tipo);

    ctx.status(200).json(DonanteMapper.aDtoResumen(donantes));
    commitTransaction();
  }

  public void obtener(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonante = ctx.pathParam("id");

    beginTransaction();
    Donante donante = buscarDonantePorId(idDonante);

    ctx.status(200).json(DonanteMapper.aDto(donante));
    commitTransaction();
  }

  public void actualizar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonante = ctx.pathParam("id");
    //Cosas que recibo por Body
    DonanteRequest donanteDto = ctx.bodyAsClass(DonanteRequest.class);

    beginTransaction();
    Donante donante = buscarDonantePorId(idDonante);
    DonanteMapper.actualizarDesdeRequest(donante, donanteDto);
    repoDonantes.actualizar(donante);
    commitTransaction();

    ctx.status(200).json("Donante actualizado correctamente");
  }

  public void eliminar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonante = ctx.pathParam("id");

    beginTransaction();
    Donante donante = buscarDonantePorId(idDonante);
    repoDonantes.eliminar(donante);
    commitTransaction();

    ctx.status(204);
  }

  //================= FUNCIONES AUXILIARES ========================
  private Donante buscarDonantePorId(String id) {
    Donante donante = repoDonantes.buscarPorId(Long.valueOf(id));
    if (donante == null) throw new RecursoNoEncontradoException("No existe donante: " + id);
    return donante;
  }

}
