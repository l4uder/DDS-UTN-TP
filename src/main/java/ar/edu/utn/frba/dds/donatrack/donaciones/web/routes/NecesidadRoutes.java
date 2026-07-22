package ar.edu.utn.frba.dds.donatrack.donaciones.web.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.NecesidadController;
import io.javalin.Javalin;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NecesidadRoutes {
  private static final NecesidadController controller = new NecesidadController();

  public static void registrar(Javalin app) {
    app.post("/beneficiarios/{id}/necesidades", controller::crear);
    app.get("/beneficiarios/{id}/necesidades", controller::obtenerTodos);
    app.get("/beneficiarios/{id}/necesidades/{nid}", controller::obtener);
    app.patch("/beneficiarios/{id}/necesidades/{nid}", controller::actualizar);
    app.delete("/beneficiarios/{id}/necesidades/{nid}", controller::eliminar);
  }

}
