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
    app.post("/donaciones/{id}/estados", controller::cambiarEstado); //aca lo modelamos como post ya que en realidad no es un cambio de estado de true a false, sino que agrega un estado a la lista historialdeEstado (por eso no usamos patch)
    app.get("/donaciones/{id}/estados", controller::listarEstados);
  }
}
