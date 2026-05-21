package ar.edu.utn.frba.dds.donatrack.notificacion.servicioCorreo;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.notificacion.Notificador;

public interface ServicioCorreo {
    public void notificar(String correo, String mensaje);
}
