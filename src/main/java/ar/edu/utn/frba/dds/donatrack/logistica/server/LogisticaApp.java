package ar.edu.utn.frba.dds.donatrack.logistica.server;

import ar.edu.utn.frba.dds.donatrack.logistica.coordinadores.CoordinadorEntrega;
import ar.edu.utn.frba.dds.donatrack.logistica.coordinadores.CoordinadorRuta;
import ar.edu.utn.frba.dds.donatrack.logistica.cron.ProcesoLogistica;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion.PlanificadorLogistico;
import ar.edu.utn.frba.dds.donatrack.logistica.integracion.DonacionesClient;
import ar.edu.utn.frba.dds.donatrack.logistica.controller.RutaController;
import ar.edu.utn.frba.dds.donatrack.logistica.controller.EntregaController;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.RutaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.routes.RutaRoutes;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
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

    app.get("/health",
        ctx -> ctx.json(new Health("logistica-service", "OK"))
    );

    CamionRoutes.registrar(app);

    DonacionesClient donacionesClient = new DonacionesClient();

    EntregaRepository entregaRepository = EntregaRepository.getInstancia();
    CamionRepository camionRepository = CamionRepository.getInstancia();
    RutaRepository rutaRepository = RutaRepository.getInstancia();

    CoordinadorEntrega coordinadorEntrega = new CoordinadorEntrega(
        entregaRepository,
        donacionesClient
    );

    EntregaController entregaController = new EntregaController(
        entregaRepository,
        coordinadorEntrega
    );

    PlanificadorLogistico planificador = new PlanificadorLogistico();

    CoordinadorRuta coordinadorRuta = new CoordinadorRuta(
        rutaRepository,
        camionRepository,
        entregaRepository,
        donacionesClient,
        planificador
    );

    RutaController rutaController = new RutaController(
        rutaRepository,
        coordinadorRuta
    );

    RutaRoutes.registrar(app, rutaController, entregaController);

    ProcesoLogistica procesoLogistica = new ProcesoLogistica(coordinadorRuta);
    app.post("/admin/planificar", ctx -> {
      procesoLogistica.ejecutar();
      ctx.status(200);
    });

    return app;
  }

  public record Health(
      String servicio,
      String estado
  ) {}

}