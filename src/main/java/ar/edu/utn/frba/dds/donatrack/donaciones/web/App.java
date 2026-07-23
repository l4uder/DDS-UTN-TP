package ar.edu.utn.frba.dds.donatrack.donaciones.web;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.notificacion.Notificador;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.AsignacionRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.BeneficiarioRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.DonacionRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.DonanteRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.NecesidadRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.notificacion.AppEventBus;
import ar.edu.utn.frba.dds.donatrack.shared.ConfiguracionEntorno;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers;
import ar.edu.utn.frba.dds.donatrack.shared.GsonConfig;
import io.javalin.Javalin;

public class App {

  public static final int PUERTO = 7070;

  public static void main(String[] args) {
    ConfiguracionEntorno config = ConfiguracionEntorno.getInstance();
    Notificador.init(AppEventBus.getInstance());
    crearApp().start(config.puertoDonaciones(PUERTO));
  }

  public static Javalin crearApp() {
    Javalin app = Javalin.create(config -> {
      config.jsonMapper(GsonConfig.jsonMapper());
      config.http.defaultContentType = "application/json";
    });

    ExceptionHandlers.registrar(app);

    app.get("/health", ctx -> ctx.json("Micro servicio de donaciones funcionando"));
    DonanteRoutes.registrar(app);
    DonacionRoutes.registrar(app);
    BeneficiarioRoutes.registrar(app);
    NecesidadRoutes.registrar(app);
    AsignacionRoutes.registrar(app);

    return app;
  }

}
