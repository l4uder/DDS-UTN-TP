package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import java.util.concurrent.Executors;

public class DispatcherEventos {
  private static final DispatcherEventos INSTANCE = new DispatcherEventos();
  private final EventBus eventBus;

  private DispatcherEventos() {
    this.eventBus = new AsyncEventBus(Executors.newFixedThreadPool(3));
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
