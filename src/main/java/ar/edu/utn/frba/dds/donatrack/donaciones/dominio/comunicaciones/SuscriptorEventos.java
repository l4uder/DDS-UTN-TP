package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.eventos.EventoAsignacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.eventos.EventoEntregaExitosa;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.eventos.EventoEntregaFallida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.eventos.EventoInicioDeRuta;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.eventos.EventoVencida;
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
  public void onAsignacionDeDonacion(EventoAsignacion evento) {
    String mensajeBeneficiario = "Se le ha asignado una nueva donación: %s ".formatted(evento.detalleDonacion());
    String mensajeDonante = "Su donación se le ha asignado a: %s ".formatted(evento.beneficiario().getRazonSocial());
    evento.beneficiario().recibirNotificacion(mensajeBeneficiario);
    evento.donantes().forEach(d -> d.recibirNotificacion(mensajeDonante));
  }

  @Subscribe
  public void onInicioDeRuta(EventoInicioDeRuta evento) {
    String mensajeBeneficiario = "Su donación %s ha iniciado el viaje, link al mapa %s ".formatted(evento.detalleDonacion(), evento.linkMapa());
    String mensajeDonante = "Su donación a: %s esta en camino ".formatted(evento.beneficiario().getRazonSocial());
    evento.beneficiario().recibirNotificacionImportante(mensajeBeneficiario);
    evento.donantes().forEach(d -> d.recibirNotificacionImportante(mensajeDonante));
  }

  @Subscribe
  public void onEntregaExitosa(EventoEntregaExitosa evento) {
    String mensajeBeneficiario = "Su donación %s ya fue recibida Link del comprobante %s ".formatted(evento.detalleDonacion(), evento.linkComprobanteEntrega());
    String mensajeDonante = "La donación a: %s fue entregada con éxito ".formatted(evento.beneficiario().getRazonSocial());
    evento.beneficiario().recibirNotificacion(mensajeBeneficiario);
    evento.donantes().forEach(d -> d.recibirNotificacion(mensajeDonante));
  }

  @Subscribe
  public void onEntregaFallida(EventoEntregaFallida evento) {
    String mensajeBeneficiario = "La entrega de su donación %s no pudo concretarse por el motivo: %s ".formatted(evento.donacion().getDescripcion(), evento.observacion());
    String mensajeDonante = "La entrega de su donación a: %s no pudo concretarse por el motivo: %s ".formatted(evento.donacion().getBeneficiario().getRazonSocial(), evento.observacion());
    String mensajeAdmins = "La donación: %s no pudo ser enviada por el motivo: %s ".formatted(evento.donacion().getId(), evento.observacion());
    evento.donacion().getBeneficiario().recibirNotificacion(mensajeBeneficiario);
    evento.donacion().getDonantes().forEach(d -> d.recibirNotificacion(mensajeDonante));
    repoContactosAdmin.buscarTodos().forEach(ca -> ca.enviarMensaje(mensajeAdmins));
  }

  @Subscribe
  public void onVencida(EventoVencida evento) {
    String mensajeDonante = "La donación se venció, lo sentimos mucho";
    evento.donantes().forEach(d -> d.recibirNotificacion(mensajeDonante));
  }

}