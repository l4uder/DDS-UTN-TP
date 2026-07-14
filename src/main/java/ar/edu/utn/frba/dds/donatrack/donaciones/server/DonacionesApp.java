package ar.edu.utn.frba.dds.donatrack.donaciones.server;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ClienteCorreoRealJavaMail;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ProveedorClienteCorreo;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.notificacion.Notificador;
import ar.edu.utn.frba.dds.donatrack.donaciones.routes.AsignacionRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.routes.BeneficiarioRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.routes.DonacionRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.routes.DonanteRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.routes.NecesidadRoutes;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers;
import ar.edu.utn.frba.dds.donatrack.shared.GsonConfig;
import io.javalin.Javalin;

public class DonacionesApp {

  public static final int PUERTO = 7070;

  public static void main(String[] args) {
    ProveedorClienteCorreo.inicializar(new ClienteCorreoRealJavaMail(
        "donatrack.sistema@gmail.com", 
        "uawj zaab mxhy bmuo"
    ));

    Notificador.init(AppEventBus.getInstance());
    crearApp().start(PUERTO);
  }

  public static Javalin crearApp() {
    Javalin app = Javalin.create(config -> {
      config.jsonMapper(GsonConfig.jsonMapper());
      config.http.defaultContentType = "application/json";
    });

    ExceptionHandlers.registrar(app);

    app.get("/health", ctx -> ctx.json(new Health("donaciones-service", "OK")));
    DonanteRoutes.registrar(app);
    DonacionRoutes.registrar(app);
    BeneficiarioRoutes.registrar(app);
    NecesidadRoutes.registrar(app);
    AsignacionRoutes.registrar(app);

    return app;
  }

  public record Health(String servicio, String estado) {
  }

}
