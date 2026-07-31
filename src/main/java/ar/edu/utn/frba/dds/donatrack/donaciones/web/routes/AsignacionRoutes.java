package ar.edu.utn.frba.dds.donatrack.donaciones.web.routes;

import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.RankingRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.AsignacionController;
import io.javalin.Javalin;

public class AsignacionRoutes {

  private AsignacionRoutes() {}

  public static void registrar(Javalin app, AsignacionController controller) {
    app.post("/rankings", controller::crearRankings);
    app.get("/rankings", controller::obtenerTodos);
    app.get("/rankings/{id}", controller::obtener);
    app.patch("/rankings/{id}", controller::confirmar);//paso 2 Asignación Realizada
  }

}
