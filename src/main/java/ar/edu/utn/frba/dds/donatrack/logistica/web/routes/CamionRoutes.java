package ar.edu.utn.frba.dds.donatrack.logistica.web.routes;

import ar.edu.utn.frba.dds.donatrack.logistica.web.controller.CamionController;
import io.javalin.Javalin;

public class CamionRoutes {
  private CamionRoutes() {
  }

  public static void registrar(Javalin app) {
    CamionController controller = new CamionController();

    app.get("/camiones", controller::listar);
    app.post("/camiones", controller::crear);
    app.get("/camiones/{patente}", controller::obtener);
    app.put("/camiones/{patente}", controller::actualizar);
    app.delete("/camiones/{patente}", controller::eliminar);
  }
}
