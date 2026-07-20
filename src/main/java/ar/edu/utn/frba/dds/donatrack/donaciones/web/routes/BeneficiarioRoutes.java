package ar.edu.utn.frba.dds.donatrack.donaciones.web.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.BeneficiarioController;
import io.javalin.Javalin;

public class BeneficiarioRoutes {
  private static final BeneficiarioController controller = new BeneficiarioController();

  private BeneficiarioRoutes() {}

  public static void registrar(Javalin app) {
    app.post("/beneficiarios", controller::crear);
    app.get("/beneficiarios", controller::obtenerTodos);
    app.get("/beneficiarios/{id}", controller::obtener);
    app.patch("/beneficiarios/{id}", controller::actualizar);
    app.delete("/beneficiarios/{id}", controller::eliminar);
  }
}
