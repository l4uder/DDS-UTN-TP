package ar.edu.utn.frba.dds.donatrack.donaciones.web.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.DonacionController;
import io.javalin.Javalin;

public class DonacionRoutes {

  private DonacionRoutes() {}

  public static void registrar(Javalin app, DonacionController controller) {
    app.post("/donaciones", controller::crear);//paso 1 En Depósito -- el paso 2 está en el proceso matchmaking
    app.get("/donaciones", controller::obtenerTodos);
    app.get("/donaciones/{id}", controller::obtener);
    app.patch("/donaciones/{id}", controller::actualizar);
    app.delete("/donaciones/{id}", controller::eliminar);
    app.get("/donaciones/{id}/estados", controller::historialEstados);
    app.patch("/donaciones/{id}/lista-para-entregar", controller::donacionListaParaEntregar); //paso 3 Lista Para Entregar
    app.patch("/donaciones/{id}/en-camino", controller::donacionEnCamino); //paso 4 En Traslado
    app.patch("/donaciones/{id}/entregada", controller::donacionEntregada); //paso 5 Entregada (Fin)
    app.patch("/donaciones/{id}/error-entrega", controller::donacionEntregaFallida); //paso 5B
    app.patch("/donaciones/{id}/vuelta-deposito", controller::donacionDevueltaADeposito); //paso 6B que viene de 5B
    app.patch("/donaciones/{id}/vencida", controller::donacionVencida); //paso 2B (Fin)
  }

}
