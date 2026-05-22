package ar.edu.utn.frba.dds.donatrack.notificacion;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicio.whatsapp.ServicioWhatsapp;
import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import java.util.List;

public class Whatsapp implements Notificador {
  private ServicioWhatsapp servicioWhatsapp;

  public Whatsapp(ServicioWhatsapp servicioWhatsapp) {
    this.servicioWhatsapp = servicioWhatsapp;
  }

  @Override
  public void notificar(Donante donante, String mensaje) {
    List<MedioContacto> mediosDeContacto = donante.getMediosContacto(TipoContacto.WHATSAPP);
    List<String> numerosaEnviar = mediosDeContacto.stream().map(MedioContacto::getDetalle).toList();

    numerosaEnviar.forEach(numero -> servicioWhatsapp.notificar(numero, mensaje));
  }
}
