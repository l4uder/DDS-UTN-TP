package ar.edu.utn.frba.dds.donatrack.donaciones.web.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.DonacionController;
import io.javalin.Javalin;

public class DonacionRoutes {
  private static final DonacionController controller = new DonacionController(DonacionRepository.getInstancia(), DonanteRepository.getInstancia());

  private DonacionRoutes() {}

  public static void registrar(Javalin app) {
    app.post("/donaciones", controller::crear);
    app.get("/donaciones", controller::obtenerTodos);
    app.get("/donaciones/{id}", controller::obtener);
    app.patch("/donaciones/{id}", controller::actualizar);
    app.delete("/donaciones/{id}", controller::eliminar);
    app.get("/donaciones/{id}/estados", controller::historialEstados);
    app.patch("/donaciones/{id}/vencida", controller::cambiarEstadoAVencida);//paso 1B (el paso 1 está en el proceso matchmaking)
    app.patch("/donaciones/{id}/lista-para-entregar", controller::donacionListaParaEntregar);//paso 2
    app.patch("/donaciones/{id}/en-ruta", controller::donacionEnTraslado);//paso 3
    app.patch("/donaciones/{id}/entregada", controller::donacionEntregada);//paso 4A
    app.patch("/donaciones/{id}/error-entrega", controller::donacionEntregaFallida);//paso 4B
    app.patch("/donaciones/{id}/vuelta-deposito", controller::donacionDevueltaADeposito); //paso 5 viene de 4B



  }

}
