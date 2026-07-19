package ar.edu.utn.frba.dds.donatrack.donaciones.web.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.DonanteController;
import io.javalin.Javalin;

public class DonanteRoutes {

  private DonanteRoutes() {
  }

  public static void registrar(Javalin app) {
    DonanteController controller = new DonanteController();

    app.get("/donantes", controller::listar);
    app.post("/donantes", controller::crear);
    app.get("/donantes/{id}", controller::obtener);
    app.put("/donantes/{id}", controller::actualizar);
    app.delete("/donantes/{id}", controller::eliminar);
  }

}
