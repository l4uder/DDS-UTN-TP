package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.notificacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoAsignacionDeDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoEntregaExitosa;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoEntregaFallida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoInicioDeRuta;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.AdministratorRepository;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.time.format.DateTimeFormatter;

public class Notificador {
  private static Notificador INSTANCE;

  public static void init(EventBus eventBus) {
    if (INSTANCE == null) {
      INSTANCE = new Notificador(eventBus);
    }
  }

  private Notificador(EventBus eventBus) {
    eventBus.register(this);
  }

  @Subscribe
  public void onAsignacionDeDonacion(EventoAsignacionDeDonacion evento) {
  }

  @Subscribe
  public void onEntregaFallida(EventoEntregaFallida evento) {
    evento.donacion().getBeneficiario().recibirNotificacion(
        "Su donacion asignada %s no pudo ser enviada por el siguiente problema %s.".formatted(evento.donacion().getId(), evento.observacion())
    );

    // Iteramos directamente sobre los objetos Donante
    evento.donacion().getDonantes().forEach(d -> d.recibirNotificacion(
        "Su donacion a %s no pudo ser entregada debido al siguiente problema: %s".formatted(
            evento.donacion().getBeneficiario().getRazonSocial(), evento.observacion()
        )
    ));

    AdministratorRepository.getInstancia().buscarTodos()
        .forEach(
            adm -> adm.notificar("Su donacion asignada %s no pudo ser enviada por el siguiente problema %s.".formatted(
                evento.donacion().getId(), evento.observacion())
            )
        );
  }

  @Subscribe
  public void onEntregaExitosa(EventoEntregaExitosa evento) {
    evento.donacion().getBeneficiario().recibirNotificacion(
        "Su donacion asignada %s fue entregada con exito con fecha %s.".formatted(
            evento.donacion().getId(), evento.date().format(DateTimeFormatter.BASIC_ISO_DATE)
        )
    );

    // Iteramos directamente sobre los objetos Donante
    evento.donacion().getDonantes().forEach(d -> d.recibirNotificacion(
        "Su donacion %s fue entregada con éxito al beneficiario %s con fecha %s.".formatted(
            evento.donacion().getId(), evento.donacion().getBeneficiario().getRazonSocial(),
            evento.date().format(DateTimeFormatter.BASIC_ISO_DATE)
        )
    ));
  }

  @Subscribe
  public void onInicioDeRuta(EventoInicioDeRuta evento) {
    evento.donacion().getBeneficiario().recibirNotificacion(
        "Su donacion asignada %s ha iniciado el viaje, link al mapa %s.".formatted(
            evento.donacion().getId(), evento.linkMapa()
        )
    );

    // Iteramos directamente sobre los objetos Donante
    evento.donacion().getDonantes().forEach(d -> d.recibirNotificacion(
        "Su donacion %s ha iniciado el viaje al beneficiario %s, link al mapa %s.".formatted(
            evento.donacion().getId(), evento.donacion().getBeneficiario().getRazonSocial(),
            evento.linkMapa()
        )
    ));
  }
}