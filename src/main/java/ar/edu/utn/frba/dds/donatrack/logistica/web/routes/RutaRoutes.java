package ar.edu.utn.frba.dds.donatrack.logistica.web.routes;

import ar.edu.utn.frba.dds.donatrack.logistica.web.controller.RutaController;
import io.javalin.Javalin;

public class RutaRoutes {
  private RutaRoutes() {}

  public static void registrar(Javalin app, RutaController rutaController) {
    app.get("/rutas", rutaController::listar);
    app.get("/rutas/{id}", rutaController::obtener);
    app.post("/rutas/{id}/chofer", rutaController::asignarChofer);
    app.post("/rutas/{id}/inicio", rutaController::iniciar);
  }
}