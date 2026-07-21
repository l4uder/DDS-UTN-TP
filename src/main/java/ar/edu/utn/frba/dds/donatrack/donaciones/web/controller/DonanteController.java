package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDonante;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.DonanteMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;

public class DonanteController {
  private final DonanteRepository repoDonantes;

  public DonanteController() {
    this.repoDonantes = DonanteRepository.getInstancia();
  }

  public void crear(Context ctx) {
    try {
      //Cosas que recibo por Body
      DonanteRequest donanteDto = ctx.bodyAsClass(DonanteRequest.class);

      Donante donante = DonanteMapper.aDominio(donanteDto);

      repoDonantes.guardar(donante);
      ctx.status(201).json(DonanteMapper.aDto(donante));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void obtenerTodos(Context ctx) {
    try {
      //Cosas que recibo por URL --> Query param
      String tipo = ctx.queryParam("tipo");

      TipoDonante tipoDonante = aTipoDonante(tipo);

      List<Donante> donantes = (tipoDonante == null) ? repoDonantes.buscarTodos() : repoDonantes.buscarPorTipo(tipoDonante);
      ctx.json(donantes.stream().map(DonanteMapper::aDtoResumen).toList());
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void obtener(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonante = ctx.pathParam("id");

      Donante donante = buscarDonantePorId(idDonante);

      ctx.json(DonanteMapper.aDto(donante));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void actualizar(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonante = ctx.pathParam("id");
      //Cosas que recibo por Body
      DonanteRequest donanteDto = ctx.bodyAsClass(DonanteRequest.class);

      Donante donante = buscarDonantePorId(idDonante);
      DonanteMapper.actualizarDominio(donante, donanteDto);

      repoDonantes.actualizar(donante);
      ctx.json(DonanteMapper.aDto(donante));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void eliminar(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonante = ctx.pathParam("id");

      Donante donante = buscarDonantePorId(idDonante);

      repoDonantes.eliminar(idDonante);
      ctx.status(204);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  private TipoDonante aTipoDonante(String tipo) {
    if (tipo == null || tipo.isBlank()) return null;
    try {
      return TipoDonante.valueOf(tipo.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DomainValidationException("El tipo: " + tipo + " no existe");
    }
  }

  //================= FUNCIONES AUXILIARES ========================
  private Donante buscarDonantePorId(String id) {
    Donante donante = repoDonantes.buscarPorId(id);
    if (donante == null) throw new RecursoNoEncontradoException("No existe donante: " + id);
    return donante;
  }

}
