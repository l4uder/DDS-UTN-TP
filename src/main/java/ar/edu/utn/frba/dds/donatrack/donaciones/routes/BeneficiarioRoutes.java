package ar.edu.utn.frba.dds.donatrack.donaciones.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.controller.BeneficiarioController;
import io.javalin.Javalin;

public class BeneficiarioRoutes {
  private BeneficiarioRoutes() {
  }

  public static void registrar(Javalin app) {
    BeneficiarioController controller = new BeneficiarioController();

    app.get("/beneficiarios", controller::listar);
    app.post("/beneficiarios", controller::crear);
    app.get("/beneficiarios/{id}", controller::obtener);
    app.put("/beneficiarios/{id}", controller::actualizar);
    app.delete("/beneficiarios/{id}", controller::eliminar);
  }
}
