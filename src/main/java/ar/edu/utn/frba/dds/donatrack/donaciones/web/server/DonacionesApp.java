package ar.edu.utn.frba.dds.donatrack.donaciones.web.server;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo.ClienteCorreoRealJavaMail;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ProveedorClienteCorreo;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.notificacion.Notificador;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.AsignacionRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.BeneficiarioRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.DonacionRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.DonanteRoutes;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.routes.NecesidadRoutes;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers;
import ar.edu.utn.frba.dds.donatrack.shared.GsonConfig;
import io.javalin.Javalin;
import io.github.cdimascio.dotenv.Dotenv;

public class DonacionesApp {

  public static final int PUERTO = 7070;

  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.load();
    String password = dotenv.get("DONATRACK_EMAIL_PASSWORD");

    if (password == null || password.isEmpty()) {
        throw new RuntimeException("Falta configurar la variable de entorno DONATRACK_EMAIL_PASSWORD");
    }

    ProveedorClienteCorreo.inicializar(new ClienteCorreoRealJavaMail(
        "donatrack.sistema@gmail.com", 
        password
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
