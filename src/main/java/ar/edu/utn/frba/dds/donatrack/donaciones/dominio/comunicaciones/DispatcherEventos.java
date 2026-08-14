package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicacionescambioestado;

import com.google.common.eventbus.EventBus;

public final class DispatcherEventos extends EventBus {
  private static final DispatcherEventos INSTANCE = new DispatcherEventos();

  private DispatcherEventos() { }

  public static DispatcherEventos getInstance(){
    return INSTANCE;
  }

}
