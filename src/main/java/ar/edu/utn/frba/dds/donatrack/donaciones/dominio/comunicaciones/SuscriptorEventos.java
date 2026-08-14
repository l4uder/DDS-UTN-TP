package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicacionescambioestado;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicacionescambioestado.eventos.EventoEntregaExitosa;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicacionescambioestado.eventos.EventoEntregaFallida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicacionescambioestado.eventos.EventoInicioDeRuta;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.ContactosAdminRepository;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class SuscriptorEventos {
  private static SuscriptorEventos INSTANCE;
  private static ContactosAdminRepository repoContactosAdmin;

  public static void init(EventBus eventBus) {
    if (INSTANCE == null) {
      INSTANCE = new SuscriptorEventos(eventBus);
      repoContactosAdmin = ContactosAdminRepository.getInstancia();
    }
  }

  private SuscriptorEventos(EventBus eventBus) {
    eventBus.register(this);
  }

  @Subscribe
  public void onInicioDeRuta(EventoInicioDeRuta evento) {
    evento.beneficiario().recibirNotificacionImportante(
        "Link al mapa: " + evento.linkMapa() +
            " de su donación: " + evento.detalleDonacion()
    );
  }

  @Subscribe
  public void onEntregaExitosa(EventoEntregaExitosa evento) {
    evento.beneficiario().recibirNotificacionImportante(
        "Link al comprobante: " + evento.linkComprobanteEntrega() +
            " de la donación: " + evento.detalleDonacion()
    );

    evento.donantes().forEach(d -> d.recibirNotificacion(
        "Link al comprobante: " + evento.linkComprobanteEntrega() +
            " de la donación: " + evento.detalleDonacion())
    );
  }

  @Subscribe
  public void onEntregaFallida(EventoEntregaFallida evento) {
    repoContactosAdmin.buscarTodos().forEach(c ->
        c.notificar("La donación: %s no pudo ser enviada por: %s ".formatted(evento.donacionId(), evento.observacion()))
    );
  }

}