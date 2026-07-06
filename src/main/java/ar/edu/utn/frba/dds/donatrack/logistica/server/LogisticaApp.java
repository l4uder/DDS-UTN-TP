package ar.edu.utn.frba.dds.donatrack.logistica.server;

import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers;
import ar.edu.utn.frba.dds.donatrack.shared.GsonConfig;
import io.javalin.Javalin;

public class LogisticaApp {

  public static final int PUERTO = 7071;

  public static void main(String[] args) {
    crearApp().start(PUERTO);
  }

  public static Javalin crearApp() {
    Javalin app = Javalin.create(config -> {
      config.jsonMapper(GsonConfig.jsonMapper());
      config.http.defaultContentType = "application/json";
    });

    ExceptionHandlers.registrar(app);

    app.get("/health", ctx -> ctx.json(new Health("logistica-service", "OK")));

    return app;
  }

  public record Health(String servicio, String estado) {
  }

}
