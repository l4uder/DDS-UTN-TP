package ar.edu.utn.frba.dds.donatrack.logistica.web.routes;

import ar.edu.utn.frba.dds.donatrack.logistica.web.controller.EntregaController;
import io.javalin.Javalin;

public class EntregaRoutes {

  private EntregaRoutes() {}

  public static void registrar(Javalin app, EntregaController entregaController) {
    app.get("/entregas", entregaController::obtenerTodos);
    app.get("/entregas/{id}", entregaController::obtener);
    app.patch("/entregas/{id}/foto", entregaController::agregarFoto);
    app.patch("/entregas/{id}/recibida", entregaController::confirmarRecibida); // activa el paso 5 de la donación (donación entregada).
    app.patch("/entregas/{id}/no-recibida", entregaController::confirmarNoRecibida); // activa el paso 5B de la donación (error al entregar la donación).
    app.patch("/entregas/{id}/reingreso", entregaController::reingresarADeposito); // activa el paso 6 de la donación (donación devuelta a depósito).
  }

}
