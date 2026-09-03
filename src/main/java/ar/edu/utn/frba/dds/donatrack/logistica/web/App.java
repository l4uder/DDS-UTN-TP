package ar.edu.utn.frba.dds.donatrack.logistica.web;

import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.DatosIniciales;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.GpsRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.web.controllers.CamionController;
import ar.edu.utn.frba.dds.donatrack.logistica.web.controllers.PlanificacionController;
import ar.edu.utn.frba.dds.donatrack.logistica.web.coordinadores.CoordinadorRuta;
import ar.edu.utn.frba.dds.donatrack.logistica.cron.ProcesoLogistica;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.planificadorexterno.ClientePlanificadorExterno;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.planificadorexterno.ClientePlanificadorExternoMock;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.microserviciosdonaciones.ConectorDonacionesApi;
import ar.edu.utn.frba.dds.donatrack.logistica.web.controllers.RutaController;
import ar.edu.utn.frba.dds.donatrack.logistica.web.controllers.EntregaController;
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
    DatosIniciales.init();
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

    //Servicios Externos
    ConectorDonacionesApi donacionesClient = new ConectorDonacionesApi();
    ClientePlanificadorExterno clienteExterno = new ClientePlanificadorExternoMock();

    //Repositorios
    EntregaRepository entregaRepository = EntregaRepository.getInstancia();
    CamionRepository camionRepository = CamionRepository.getInstancia();
    RutaRepository rutaRepository = RutaRepository.getInstancia();
    GpsRepository gpsRepository = GpsRepository.getInstancia();

    //Coordinadores y procesos
    CoordinadorRuta coordinadorRuta = new CoordinadorRuta(
        rutaRepository, camionRepository, entregaRepository,
        donacionesClient, clienteExterno
    );
    ProcesoLogistica procesoLogistica = new ProcesoLogistica(coordinadorRuta);

    //Controllers
    PlanificacionController planificacionController = new PlanificacionController(coordinadorRuta, procesoLogistica);
    RutaController rutaController = new RutaController(rutaRepository, donacionesClient);
    EntregaController entregaController = new EntregaController(entregaRepository, donacionesClient);
    CamionController camionController = new CamionController(camionRepository, gpsRepository);

    //Registramos las Rutas
    app.get("/health",ctx -> ctx.json("Micro servicio de logística funcionando"));
    CamionRoutes.registrar(app, camionController);
    RutaRoutes.registrar(app, rutaController);
    EntregaRoutes.registrar(app, entregaController);
    PlanificacionRoutes.registrar(app, planificacionController);

    //Registramos las excepciones
    ExceptionHandlers.registrar(app);

    return new AppLogistica(app, procesoLogistica);
  }

  public record AppLogistica(Javalin app, ProcesoLogistica procesoLogistica) {}

}