package ar.edu.utn.frba.dds.donatrack.logistica.web.routes;

import ar.edu.utn.frba.dds.donatrack.logistica.web.controllers.RutaController;
import io.javalin.Javalin;

public class RutaRoutes {

  private RutaRoutes() {}

  public static void registrar(Javalin app, RutaController rutaController) {
    app.get("/rutas", rutaController::obtenerTodas);
    app.get("/rutas/{id}", rutaController::obtener);
    app.patch("/rutas/{id}/chofer", rutaController::asignarChofer);
    app.patch("/rutas/{id}/inicio", rutaController::iniciar); //activa el paso 4 de la donación (En Traslado)
  }

}