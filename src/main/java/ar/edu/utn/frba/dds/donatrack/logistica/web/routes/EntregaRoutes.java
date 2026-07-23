package ar.edu.utn.frba.dds.donatrack.logistica.web.routes;

import ar.edu.utn.frba.dds.donatrack.logistica.web.controller.EntregaController;
import io.javalin.Javalin;

public class EntregaRoutes {
  private EntregaRoutes() {}

  public static void registrar(Javalin app, EntregaController entregaController) {
    app.get("/entregas", entregaController::listar);
    app.get("/entregas/{id}", entregaController::obtener);
    app.post("/entregas/{id}/recepcion-confirmada", entregaController::confirmarRecepcion);
    app.post("/entregas/{id}/no-recibida", entregaController::marcarNoRecibida);
    app.post("/entregas/{id}/reingreso", entregaController::reingresarADeposito);
    app.post("/entregas/{id}/fotos", entregaController::agregarFoto);
  }
}
