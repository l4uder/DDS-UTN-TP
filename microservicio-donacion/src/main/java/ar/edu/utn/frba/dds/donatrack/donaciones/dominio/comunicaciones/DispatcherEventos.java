package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones;

import com.google.common.eventbus.EventBus;

public class DispatcherEventos {
  private static final DispatcherEventos INSTANCE = new DispatcherEventos();
  private final EventBus eventBus;

  private DispatcherEventos() {
    this.eventBus = new EventBus();
  }

  public static DispatcherEventos getInstancia(){
    return INSTANCE;
  }

  public void publicar(Object evento) {
    this.eventBus.post(evento);
  }

  public void suscribir(Object suscriptor) {
    this.eventBus.register(suscriptor);
  }

}
