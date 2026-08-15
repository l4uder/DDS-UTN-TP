package ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class ResultadoPlanificacion {
  private Map<Camion, List<Entrega>> entregasPorCamion;
  private List<Entrega> entregasSinAsignar;

  public ResultadoPlanificacion(Map<Camion, List<Entrega>> entregasPorCamion, List<Entrega> entregasSinAsignar) {
    this.entregasPorCamion = entregasPorCamion;
    this.entregasSinAsignar = entregasSinAsignar;
  }

}