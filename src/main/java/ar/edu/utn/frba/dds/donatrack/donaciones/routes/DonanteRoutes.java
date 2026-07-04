package ar.edu.utn.frba.dds.donatrack.donaciones.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.controller.DonanteController;
import ar.edu.utn.frba.dds.donatrack.donaciones.service.DonanteService;
import io.javalin.Javalin;

public class DonanteRoutes {

  private DonanteRoutes() {
  }

  public static void registrar(Javalin app) {
    DonanteController controller = new DonanteController(new DonanteService());

    app.get("/donantes", controller::listar);
    app.post("/donantes", controller::crear);
    app.get("/donantes/{id}", controller::obtener);
    app.put("/donantes/{id}", controller::actualizar);
    app.delete("/donantes/{id}", controller::eliminar);
  }

}
