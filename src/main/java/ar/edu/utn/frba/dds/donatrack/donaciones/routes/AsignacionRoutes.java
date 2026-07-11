package ar.edu.utn.frba.dds.donatrack.donaciones.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.controller.AsignacionController;
import ar.edu.utn.frba.dds.donatrack.donaciones.service.AsignacionService;
import io.javalin.Javalin;

public class AsignacionRoutes {
  private AsignacionRoutes() {
  }

  public static void registrar(Javalin app) {
    AsignacionController controller = new AsignacionController(new AsignacionService());

    app.post("/donaciones/{id}/matchmaking", controller::ejecutarMatchmaking);
    app.get("/donaciones/{id}/ranking", controller::obtenerRanking);
    app.post("/donaciones/{id}/asignacion", controller::confirmarAsignacion);
  }
}
