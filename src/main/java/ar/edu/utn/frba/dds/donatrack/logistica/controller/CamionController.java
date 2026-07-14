package ar.edu.utn.frba.dds.donatrack.logistica.controller;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.camion.ActualizarCamionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.camion.CamionResponse;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.camion.CrearCamionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;

public class CamionController {

  private final CamionRepository repository = CamionRepository.getInstancia();

  public void listar(Context ctx) {
    List<CamionResponse> camiones = repository.buscarTodos().stream()
        .map(CamionResponse::desde)
        .toList();

    ctx.json(camiones);
  }

  public void obtener(Context ctx) {
    try {
      Camion camion = repository.obtenerPorPatente(ctx.pathParam("patente"));
      ctx.json(CamionResponse.desde(camion));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void crear(Context ctx) {
    try {
      CrearCamionRequest request =
          ctx.bodyAsClass(CrearCamionRequest.class);

      Camion camion = new Camion(
          request.patente(),
          request.capacidadVolumen(),
          request.altura(),
          request.capacidadCarga()
      );

      repository.insertar(camion);

      ctx.status(201).json(CamionResponse.desde(camion));

    } catch (JsonSyntaxException e) {
      ctx.status(400).json(
          new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    }
  }

  public void actualizar(Context ctx) {
    try {
      Camion camion =
          repository.obtenerPorPatente(ctx.pathParam("patente"));

      ActualizarCamionRequest request =
          ctx.bodyAsClass(ActualizarCamionRequest.class);

      camion.actualizarDatos(
          request.capacidadVolumen(),
          request.altura(),
          request.capacidadCarga()
      );

      repository.guardar(camion);

      ctx.json(CamionResponse.desde(camion));

    } catch (JsonSyntaxException e) {
      ctx.status(400).json(
          new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void eliminar(Context ctx) {
    try {
      String patente = ctx.pathParam("patente");

      repository.obtenerPorPatente(patente);
      repository.eliminar(patente);

      ctx.status(204);

    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }
}