package ar.edu.utn.frba.dds.donatrack.notificacion;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicio.correo.ServicioCorreo;
import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import java.util.List;

public class CorreoElectronico implements Notificador {
  private ServicioCorreo servicioCorreo;

  public CorreoElectronico(ServicioCorreo servicioCorreo) {
    this.servicioCorreo = servicioCorreo;
  }

  @Override
  public void notificar(Donante donante, String mensaje) {
    List<MedioContacto> mediosDeContacto = donante.getMediosContacto(TipoContacto.CORREO);
    List<String> correosaEnviar = mediosDeContacto.stream().map(MedioContacto::getDetalle).toList();

    correosaEnviar.forEach(correo -> servicioCorreo.notificar(correo, mensaje));
  }
}
