package ar.edu.utn.frba.dds.donatrack.donaciones.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.donante.DonanteMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.donante.DonanteRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.donante.DonanteResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;

public class DonanteController {

  private final DonanteRepository repository = DonanteRepository.getInstancia();

  public void listar(Context ctx) {
    String tipo = ctx.queryParam("tipo");
    List<Donante> todos = repository.buscarTodos();
    List<Donante> filtrados;
    if (tipo == null) {
      filtrados = todos;
    } else if (tipo.equalsIgnoreCase("HUMANA")) {
      filtrados = todos.stream().filter(d -> d instanceof PersonaHumana).toList();
    } else if (tipo.equalsIgnoreCase("JURIDICA")) {
      filtrados = todos.stream().filter(d -> d instanceof PersonaJuridica).toList();
    } else {
      ctx.status(400).json(new ErrorResponse(400, "Tipo invalido: " + tipo + " (humana o juridica)"));
      return;
    }
    ctx.json(filtrados.stream().map(DonanteMapper::aResponse).toList());
  }

  public void obtener(Context ctx) {
    try {
      ctx.json(DonanteMapper.aResponse(repository.obtenerPorId(ctx.pathParam("id"))));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void crear(Context ctx) {
    try {
      DonanteRequest request = ctx.bodyAsClass(DonanteRequest.class);
      Donante creado = DonanteMapper.aDominio(request);
      repository.guardarDonante(creado);
      ctx.status(201).json(DonanteMapper.aResponse(creado));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    }
  }

  public void actualizar(Context ctx) {
    try {
      repository.obtenerPorId(ctx.pathParam("id"));
      DonanteRequest request = ctx.bodyAsClass(DonanteRequest.class);
      Donante actualizado = DonanteMapper.aDominio(request);
      actualizado.setId(ctx.pathParam("id"));
      repository.guardarDonante(actualizado);
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
      repository.obtenerPorId(ctx.pathParam("id"));
      repository.eliminar(ctx.pathParam("id"));
      ctx.status(204);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }
}
