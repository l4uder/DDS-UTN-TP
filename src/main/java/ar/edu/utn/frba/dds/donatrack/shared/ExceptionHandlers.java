package ar.edu.utn.frba.dds.donatrack.shared;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.javalin.Javalin;

public class ExceptionHandlers {

  public record ErrorResponse(int status, String error) {
  }

  private ExceptionHandlers() {
  }

  public static void registrar(Javalin app) {
    app.exception(DomainValidationException.class, (e, ctx) ->
        ctx.status(400).json(new ErrorResponse(400, e.getMessage())));

    app.exception(CambioDeEstadoNoPermitidoException.class, (e, ctx) ->
        ctx.status(409).json(new ErrorResponse(409, e.getMessage())));

    app.exception(RecursoNoEncontradoException.class, (e, ctx) ->
        ctx.status(404).json(new ErrorResponse(404, e.getMessage())));

    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500).json(new ErrorResponse(500, "Error interno del servidor"));
      e.printStackTrace();
    });
  }

}