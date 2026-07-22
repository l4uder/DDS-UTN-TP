package ar.edu.utn.frba.dds.donatrack.logistica.web.routes;

import ar.edu.utn.frba.dds.donatrack.logistica.web.controller.PlanificacionController;
import io.javalin.Javalin;

public class PlanificacionRoutes {
  private PlanificacionRoutes() {}

  public static void registrar(Javalin app, PlanificacionController planificacionController) {
    app.post("/planificaciones/entregas-pendientes", planificacionController::planificar);
    app.post("/planificaciones/callback", planificacionController::callback);
    app.post("/planificaciones/ejecucion-manual", planificacionController::ejecutarManual);
  }
}