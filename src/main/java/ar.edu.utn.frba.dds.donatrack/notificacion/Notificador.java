package ar.edu.utn.frba.dds.donatrack.notificacion;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;

public interface Notificador {
  void notificar(Donante donante, String mensaje);
}
