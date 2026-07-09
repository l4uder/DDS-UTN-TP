package ar.edu.utn.frba.dds.donatrack.logistica.server;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.integracion.DonacionesClient;
import ar.edu.utn.frba.dds.donatrack.logistica.service.GestorRuta;
import ar.edu.utn.frba.dds.donatrack.logistica.service.GestorEntrega;
import ar.edu.utn.frba.dds.donatrack.logistica.controller.RutaController;
import ar.edu.utn.frba.dds.donatrack.logistica.controller.EntregaController;
import ar.edu.utn.frba.dds.donatrack.logistica.routes.RutaRoutes;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.RutaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.routes.CamionRoutes;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers;
import ar.edu.utn.frba.dds.donatrack.shared.GsonConfig;
import io.javalin.Javalin;

public class LogisticaApp {

  public static final int PUERTO = 7071;

  public static void main(String[] args) {
    crearApp().start(PUERTO);
  }

  public static Javalin crearApp() {
    Javalin app = Javalin.create(config -> {
      config.jsonMapper(GsonConfig.jsonMapper());
      config.http.defaultContentType = "application/json";
    });

    ExceptionHandlers.registrar(app);

    app.get("/health", ctx -> ctx.json(new Health("logistica-service", "OK")));

    CamionRoutes.registrar(app);

    DonacionesClient donacionesClient = new DonacionesClient();

    GestorRuta gestorRuta = new GestorRuta(
        RutaRepository.getInstancia(),
        CamionRepository.getInstancia(),
        EntregaRepository.getInstancia(),
        donacionesClient
    );
    GestorEntrega gestorEntrega = new GestorEntrega(
        EntregaRepository.getInstancia(),
        donacionesClient
    );
    RutaController rutaController = new RutaController(gestorRuta);
    EntregaController entregaController = new EntregaController(gestorEntrega);
    RutaRoutes.registrar(app, rutaController, entregaController);

    return app;
  }

  public record Health(String servicio, String estado) {
  }

}
