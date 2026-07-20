package ar.edu.utn.frba.dds.donatrack.donaciones.web.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.DonanteController;
import io.javalin.Javalin;

public class DonanteRoutes {
  private static final DonanteController controller = new DonanteController();

  private DonanteRoutes() {}

  public static void registrar(Javalin app) {
    app.post("/donantes", controller::crear);
    app.get("/donantes", controller::obtenerTodos);
    app.get("/donantes/{id}", controller::obtener);
    app.patch("/donantes/{id}", controller::actualizar);
    app.delete("/donantes/{id}", controller::eliminar);
  }

}
