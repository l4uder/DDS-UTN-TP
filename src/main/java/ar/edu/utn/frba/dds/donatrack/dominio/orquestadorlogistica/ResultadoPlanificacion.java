package ar.edu.utn.frba.dds.donatrack.dominio.orquestadorlogistica;

import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Camion;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Entrega;
import java.util.List;
import java.util.Map;

public class ResultadoPlanificacion {
  private Map<Camion, List<Entrega>> entregasPorCamion;
  private List<Entrega> entregasSinAsignar;

  public ResultadoPlanificacion(Map<Camion, List<Entrega>> entregasPorCamion,
                                List<Entrega> entregasSinAsignar) {
    this.entregasPorCamion = entregasPorCamion;
    this.entregasSinAsignar = entregasSinAsignar;
  }

  public Map<Camion, List<Entrega>> getEntregasPorCamion() {
    return entregasPorCamion;
  }

  public List<Entrega> getEntregasSinAsignar() {
    return entregasSinAsignar;
  }
}
