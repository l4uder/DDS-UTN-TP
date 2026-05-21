package ar.edu.utn.frba.dds.donatrack.notificacion;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donante.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donante.TipoContacto;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicioCorreo.ServicioCorreo;

import java.util.List;

public class CorreoElectronico implements Notificador {
    private ServicioCorreo servicioCorreo;

    public CorreoElectronico(ServicioCorreo servicioCorreo) {
        this.servicioCorreo = servicioCorreo;
    }

    @Override
    public void notificar(Donante donante, String mensaje) {
        List<MedioContacto> mediosDeContacto = donante.getMediosContacto(TipoContacto.CORREO);
        List<String> correosAEnviar = mediosDeContacto.stream().map(MedioContacto::getDetalle).toList();

        correosAEnviar.forEach(correo-> servicioCorreo.notificar(correo, mensaje));
    }
}
