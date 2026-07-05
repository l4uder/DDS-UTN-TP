package ar.edu.utn.frba.dds.donatrack.donaciones.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.controller.DonacionController;
import ar.edu.utn.frba.dds.donatrack.donaciones.service.DonacionService;
import io.javalin.Javalin;

public class DonacionRoutes {
  private DonacionRoutes() {
  }

  public static void registrar(Javalin app) {
    DonacionController controller = new DonacionController(new DonacionService());

    app.get("/donaciones", controller::listar);
    app.post("/donaciones", controller::crear);
    app.get("/donaciones/{id}", controller::obtener);
    app.put("/donaciones/{id}", controller::actualizar);
    app.delete("/donaciones/{id}", controller::eliminar);
  }
}
