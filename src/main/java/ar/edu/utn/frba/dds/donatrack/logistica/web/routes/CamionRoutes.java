package ar.edu.utn.frba.dds.donatrack.logistica.web.routes;

import ar.edu.utn.frba.dds.donatrack.logistica.web.controller.CamionController;
import io.javalin.Javalin;

public class CamionRoutes {
  private CamionRoutes() { }

  public static void registrar(Javalin app, CamionController camionController) {
    app.post("/camiones", camionController::crear);
    app.get("/camiones", camionController::obtenerTodos);
    app.get("/camiones/{patente}", camionController::obtener);
    app.put("/camiones/{patente}", camionController::actualizar);
    app.delete("/camiones/{patente}", camionController::eliminar);
  }

}
