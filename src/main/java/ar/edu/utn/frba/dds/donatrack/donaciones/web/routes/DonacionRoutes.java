package ar.edu.utn.frba.dds.donatrack.donaciones.web.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.DonacionController;
import io.javalin.Javalin;

public class DonacionRoutes {
  private static final DonacionController controller = new DonacionController();

  private DonacionRoutes() {}

  public static void registrar(Javalin app) {
    app.post("/donaciones", controller::crear);
    app.get("/donaciones", controller::obtenerTodos);
    app.get("/donaciones/{id}", controller::obtener);
    app.put("/donaciones/{id}", controller::actualizar);
    app.delete("/donaciones/{id}", controller::eliminar);
    //aca lo modelamos como post ya que en realidad no es un cambio de estado de true a false, sino que agrega un estado a la lista historialdeEstado (por eso no usamos patch)
    app.post("/donaciones/{id}/estado/entregada", controller::cambiarEstadoAEntregada);
    app.post("/donaciones/{id}/estado/vuelta-deposito", controller::cambiarEstadoADeposito);
    app.post("/donaciones/{id}/estado/vencida", controller::cambiarEstadoAVencida);
    app.post("/donaciones/{id}/estado/lista-para-entregar", controller::cambiarEstadoALista);
    app.post("/donaciones/{id}/estado/error-entrega", controller::cambiarEstadoAErrorEntrega);
    app.post("/donaciones/{id}/estado/en-ruta", controller::cambiarEstadoAEnTraslado);
    app.get("/donaciones/{id}/estados", controller::historialEstados);
  }
}
