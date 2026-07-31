package ar.edu.utn.frba.dds.donatrack.shared;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BodyException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.Javalin;

public class ExceptionHandlers {

  public record ErrorResponse(int status, String error) {
  }

  private ExceptionHandlers() {}

  public static void registrar(Javalin app) {
    // JSON Mal formado
    app.exception(JsonSyntaxException.class, (e, ctx) ->
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON válido")));
    // Error en el Body
    app.exception(BodyException.class, (e, ctx) ->
      ctx.status(400).json(new ErrorResponse(400, e.getMessage())));
    // Validación de Dominio
    app.exception(DomainValidationException.class, (e, ctx) ->
        ctx.status(400).json(new ErrorResponse(400, e.getMessage())));
    // Recurso no encontrado
    app.exception(RecursoNoEncontradoException.class, (e, ctx) ->
        ctx.status(404).json(new ErrorResponse(404, e.getMessage())));
    // Conflicto de estado
    app.exception(CambioDeEstadoNoPermitidoException.class, (e, ctx) ->
        ctx.status(409).json(new ErrorResponse(409, e.getMessage())));
    // Error de server
    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500).json(new ErrorResponse(500, "Error interno del servidor"));
      e.printStackTrace();
    });
  }

}