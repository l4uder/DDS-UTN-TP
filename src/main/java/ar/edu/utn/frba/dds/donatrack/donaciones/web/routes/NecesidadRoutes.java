package ar.edu.utn.frba.dds.donatrack.donaciones.web.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.NecesidadController;
import ar.edu.utn.frba.dds.donatrack.donaciones.service.NecesidadService;
import io.javalin.Javalin;

public class NecesidadRoutes {
  private static final NecesidadController controller = new NecesidadController(new NecesidadService());
  private NecesidadRoutes() {}

  public static void registrar(Javalin app) {
    app.get("/beneficiarios/{id}/necesidades", controller::listar);
    app.post("/beneficiarios/{id}/necesidades", controller::crear);
    app.get("/beneficiarios/{id}/necesidades/{nid}", controller::obtener);
    app.put("/beneficiarios/{id}/necesidades/{nid}", controller::actualizar);
    app.delete("/beneficiarios/{id}/necesidades/{nid}", controller::eliminar);
  }
}
