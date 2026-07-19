package ar.edu.utn.frba.dds.donatrack.donaciones.web.server;

import com.google.common.eventbus.EventBus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppEventBus extends EventBus {
  private static final AppEventBus INSTANCE = new AppEventBus();

  public static AppEventBus getInstance(){
    return INSTANCE;
  }
}
