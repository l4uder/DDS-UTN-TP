package ar.edu.utn.frba.dds.donatrack.donaciones.web;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.SuscriptorNotificacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.RankingRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.AsignacionController;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.BeneficiarioController;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.DonacionController;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.DonanteController;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.controller.NecesidadController;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.AsignacionRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.BeneficiarioRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.DonacionRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.DonanteRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.NecesidadRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.DispatcherEventos;
import ar.edu.utn.frba.dds.donatrack.shared.ConfiguracionEntorno;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers;
import ar.edu.utn.frba.dds.donatrack.shared.GsonConfig;
import io.javalin.Javalin;

public class App {

  public static final int PUERTO = 7070;

  public static void main(String[] args) {
    SuscriptorNotificacion.init(DispatcherEventos.getInstancia());
    crearApp().start(ConfiguracionEntorno.getInstance().puertoDonaciones(PUERTO));
  }

  public static Javalin crearApp() {
    Javalin app = Javalin.create(config -> {
      config.jsonMapper(GsonConfig.jsonMapper());
      config.http.defaultContentType = "application/json";
    });

    //Repositorios
    DonanteRepository donanteRepository = DonanteRepository.getInstancia();
    DonacionRepository donacionRepository = DonacionRepository.getInstancia();
    BeneficiarioRepository benificiarioRepository = BeneficiarioRepository.getInstancia();
    RankingRepository rankingRepository = RankingRepository.getInstancia();

    //Controllers
    DonanteController donanteController = new DonanteController(donanteRepository);
    DonacionController donacionController = new DonacionController(donacionRepository, donanteRepository);
    BeneficiarioController beneficiarioController = new BeneficiarioController(benificiarioRepository);
    NecesidadController necesidadController = new NecesidadController(benificiarioRepository);
    AsignacionController asignacionController = new AsignacionController(donacionRepository, benificiarioRepository, rankingRepository);

    //Registramos las Rutas
    app.get("/health", ctx -> ctx.json("Micro servicio de donaciones funcionando"));
    DonanteRoutes.registrar(app, donanteController);
    DonacionRoutes.registrar(app, donacionController);
    BeneficiarioRoutes.registrar(app, beneficiarioController);
    NecesidadRoutes.registrar(app, necesidadController);
    AsignacionRoutes.registrar(app, asignacionController);

    //Registramos las excepciones
    ExceptionHandlers.registrar(app);

    return app;
  }

}
