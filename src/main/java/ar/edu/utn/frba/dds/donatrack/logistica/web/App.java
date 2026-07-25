package ar.edu.utn.frba.dds.donatrack.logistica.web;

import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.GpsRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.web.controller.CamionController;
import ar.edu.utn.frba.dds.donatrack.logistica.web.controller.PlanificacionController;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.coordinadores.CoordinadorEntrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.coordinadores.CoordinadorRuta;
import ar.edu.utn.frba.dds.donatrack.logistica.cron.ProcesoLogistica;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.ClientePlanificadorExterno;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.ClientePlanificadorExternoMock;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.DonacionesClient;
import ar.edu.utn.frba.dds.donatrack.logistica.web.controller.RutaController;
import ar.edu.utn.frba.dds.donatrack.logistica.web.controller.EntregaController;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.RutaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.web.routes.EntregaRoutes;
import ar.edu.utn.frba.dds.donatrack.logistica.web.routes.PlanificacionRoutes;
import ar.edu.utn.frba.dds.donatrack.logistica.web.routes.RutaRoutes;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.web.routes.CamionRoutes;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers;
import ar.edu.utn.frba.dds.donatrack.shared.GsonConfig;
import io.javalin.Javalin;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

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

    DonacionesClient donacionesClient = new DonacionesClient();
    ClientePlanificadorExterno clienteExterno = new ClientePlanificadorExternoMock();

    EntregaRepository entregaRepository = EntregaRepository.getInstancia();
    CamionRepository camionRepository = CamionRepository.getInstancia();
    RutaRepository rutaRepository = RutaRepository.getInstancia();
    GpsRepository gpsRepository = GpsRepository.getInstancia();

    CoordinadorEntrega coordinadorEntrega = new CoordinadorEntrega(
        entregaRepository,
        donacionesClient
    );

    CoordinadorRuta coordinadorRuta = new CoordinadorRuta(
        rutaRepository, camionRepository, entregaRepository,
        donacionesClient, clienteExterno
    );

    ProcesoLogistica procesoLogistica = new ProcesoLogistica(coordinadorRuta);

    EntregaController entregaController = new EntregaController(
        entregaRepository,
        coordinadorEntrega
    );

    RutaController rutaController = new RutaController(
        rutaRepository,
        coordinadorRuta
    );

    PlanificacionController planificacionController = new PlanificacionController(
        coordinadorRuta,
        procesoLogistica
    );

    CamionController camionController = new CamionController(
        camionRepository,
        gpsRepository
    );

    app.get("/health",ctx -> ctx.json("Micro servicio de logística funcionando"));
    CamionRoutes.registrar(app, camionController);
    RutaRoutes.registrar(app, rutaController);
    EntregaRoutes.registrar(app, entregaController);
    PlanificacionRoutes.registrar(app, planificacionController);

    return new AppLogistica(app, procesoLogistica);
  }

  public record AppLogistica(Javalin app, ProcesoLogistica procesoLogistica) {}

}