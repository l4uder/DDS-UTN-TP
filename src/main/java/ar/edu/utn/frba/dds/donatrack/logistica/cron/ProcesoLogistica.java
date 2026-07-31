package ar.edu.utn.frba.dds.donatrack.logistica.cron;

import ar.edu.utn.frba.dds.donatrack.logistica.web.coordinadores.CoordinadorRuta;

public class ProcesoLogistica {

  private final CoordinadorRuta coordinadorRuta;

  public ProcesoLogistica(CoordinadorRuta coordinadorRuta) {
    this.coordinadorRuta = coordinadorRuta;
  }

  public void ejecutar() {
    System.out.println("[ProcesoLogistica] Iniciando planificación diaria de rutas...");
    coordinadorRuta.ejecutarPlanificacionDiaria();
    System.out.println("[ProcesoLogistica] Planificación diaria enviada al componente externo.");
  }
}