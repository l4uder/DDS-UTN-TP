package ar.edu.utn.frba.dds.donatrack.donaciones.web.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.AsignacionController;
import io.javalin.Javalin;

public class AsignacionRoutes {
  private static final AsignacionController controller = new AsignacionController();

  private AsignacionRoutes() {}

  public static void registrar(Javalin app) {
    app.post("/donaciones/rankings", controller::crearRankings);
    app.get("/donaciones/rankings", controller::obtenerTodos);
    app.get("/donaciones/rankings/{id}", controller::obtener);
    app.patch("/donaciones/rankings/{id}", controller::confirmar);
  }
}
