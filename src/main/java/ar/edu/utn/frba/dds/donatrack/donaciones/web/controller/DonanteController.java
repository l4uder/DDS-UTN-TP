package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoPersona;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.DonanteMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.javalin.http.Context;
import java.util.Arrays;
import java.util.List;

public class DonanteController {
  private final DonanteRepository repoDonantes;

  public DonanteController(DonanteRepository repoDonantes) {
    this.repoDonantes = repoDonantes;
  }

  public void crear(Context ctx) {
    //Cosas que recibo por Body
    DonanteRequest donanteDto = ctx.bodyAsClass(DonanteRequest.class);

    Donante donante = DonanteMapper.aDominio(donanteDto);

    repoDonantes.guardar(donante);
    ctx.status(201).json(DonanteMapper.aDto(donante));
  }

  public void obtenerTodos(Context ctx) {
    //Cosas que recibo por URL --> Query param
    String tipo = ctx.queryParam("tipo");

    TipoPersona tipoDonante = aTipoDonante(tipo);

    List<Donante> donantes = (tipoDonante == null) ? repoDonantes.buscarTodos() : repoDonantes.buscarPorTipo(tipoDonante);
    ctx.status(200).json(donantes.stream().map(DonanteMapper::aDtoResumen).toList());
  }

  public void obtener(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonante = ctx.pathParam("id");

    Donante donante = buscarDonantePorId(idDonante);

    ctx.status(200).json(DonanteMapper.aDto(donante));
  }

  public void actualizar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonante = ctx.pathParam("id");
    //Cosas que recibo por Body
    DonanteRequest donanteDto = ctx.bodyAsClass(DonanteRequest.class);

    Donante donante = buscarDonantePorId(idDonante);
    DonanteMapper.actualizarDesdeRequest(donante, donanteDto);

    repoDonantes.actualizar(donante);
    ctx.status(200).json(DonanteMapper.aDto(donante));
  }

  public void eliminar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonante = ctx.pathParam("id");

    Donante donante = buscarDonantePorId(idDonante);

    repoDonantes.eliminar(donante);
    ctx.status(204);
  }

  private TipoPersona aTipoDonante(String tipo) {
    if (tipo == null || tipo.isBlank()) return null;
    try {
      return TipoPersona.valueOf(tipo.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DominioException("El tipo de donante: " + tipo + " no existe debe ser: " + Arrays.toString(TipoPersona.values()));
    }
  }

  //================= FUNCIONES AUXILIARES ========================
  private Donante buscarDonantePorId(String id) {
    Donante donante = repoDonantes.buscarPorId(id);
    if (donante == null) throw new RecursoNoEncontradoException("No existe donante: " + id);
    return donante;
  }

}
