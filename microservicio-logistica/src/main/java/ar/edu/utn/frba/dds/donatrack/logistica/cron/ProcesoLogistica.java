package ar.edu.utn.frba.dds.donatrack.logistica.cron;

import ar.edu.utn.frba.dds.donatrack.logistica.web.coordinadores.CoordinadorRuta;

public class ProcesoLogistica {
  //Todo después cambiar esto a planificación externa, con su respectivo public static void main(String[] args)
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