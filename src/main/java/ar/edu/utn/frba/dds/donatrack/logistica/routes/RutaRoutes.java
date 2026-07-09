package ar.edu.utn.frba.dds.donatrack.logistica.routes;

import ar.edu.utn.frba.dds.donatrack.logistica.controller.RutaController;
import ar.edu.utn.frba.dds.donatrack.logistica.controller.EntregaController;
import io.javalin.Javalin;

public class RutaRoutes {
  private RutaRoutes() {}

  public static void registrar(Javalin app, RutaController rutaController,
                               EntregaController entregaController) {
    // Rutas
    app.get("/rutas", rutaController::listar);
    app.get("/rutas/{id}", rutaController::obtener);
    app.post("/rutas/{id}/chofer", rutaController::asignarChofer);
    app.post("/rutas/{id}/iniciar", rutaController::iniciarRecorrido);

    // Entregas
    app.get("/entregas", entregaController::listar);
    app.get("/entregas/{id}", entregaController::obtener);
    app.post("/entregas/{id}/confirmar", entregaController::confirmarRecepcion);
    app.post("/entregas/{id}/no-recibida", entregaController::marcarNoRecibida);
    app.post("/entregas/{id}/reingresar", entregaController::reingresarADeposito);
    app.post("/entregas/{id}/fotos", entregaController::agregarFoto);

    //Callback
    app.post("/rutas/callback-planificacion", rutaController::recibirCallback);
  }
}