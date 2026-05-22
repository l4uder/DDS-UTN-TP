package ar.edu.utn.frba.dds.donatrack.notificacion;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicio.sms.ServicioSms;
import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import java.util.List;

public class Telefono implements Notificador {
  private ServicioSms servicioSms;

  public Telefono(ServicioSms servicioSms) {
    this.servicioSms = servicioSms;
  }

  @Override
  public void notificar(Donante donante, String mensaje) {
    List<MedioContacto> mediosDeContacto = donante.getMediosContacto(TipoContacto.TELEFONO);
    List<String> numerosaEnviar = mediosDeContacto.stream().map(MedioContacto::getDetalle).toList();

    numerosaEnviar.forEach(numero -> servicioSms.notificar(numero, mensaje));
  }
}
