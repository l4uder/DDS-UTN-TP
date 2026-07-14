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
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogisticaApp {

  public static final int PUERTO = 7071;

  public static void main(String[] args) {
    AppLogistica resultado = crearApp();

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    long minutosHasta = calcularMinutosHasta(LocalTime.of(2, 0));

    scheduler.scheduleAtFixedRate(
        () -> {
          try {
            resultado.procesoLogistica().ejecutar();
          } catch (Exception e) {
            System.err.println("[Cron] Error en planificación: " + e.getMessage());
          }
        },
        minutosHasta,
        24 * 60,
        TimeUnit.MINUTES
    );

    System.out.println("[Cron] Planificación programada en " + minutosHasta + " minutos");
    resultado.app().start(PUERTO);
  }

  private static long calcularMinutosHasta(LocalTime hora) {
    long minutos = ChronoUnit.MINUTES.between(LocalTime.now(), hora);
    return minutos < 0 ? minutos + 24 * 60 : minutos;
  }

  public static AppLogistica crearApp() {
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

    return new AppLogistica(app, procesoLogistica);
  }

  public record AppLogistica(Javalin app, ProcesoLogistica procesoLogistica) {}

  public record Health(String servicio, String estado) {}

}