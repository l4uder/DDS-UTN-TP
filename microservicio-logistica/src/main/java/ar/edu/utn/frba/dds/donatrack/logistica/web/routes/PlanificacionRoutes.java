package ar.edu.utn.frba.dds.donatrack.logistica.web.routes;

import ar.edu.utn.frba.dds.donatrack.logistica.web.controllers.PlanificacionController;
import io.javalin.Javalin;

public class PlanificacionRoutes {
  private PlanificacionRoutes() {}

  public static void registrar(Javalin app, PlanificacionController planificacionController) {
    app.post("/planificaciones", planificacionController::planificar);
    app.post("/planificaciones/callback-externo", planificacionController::callback);// webhook del proveedor
    app.post("/planificaciones/manual", planificacionController::ejecutarManual);      // disparo manual del cron
  }
}