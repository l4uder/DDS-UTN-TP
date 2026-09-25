package ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.planificadorexterno;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion.Lote;
import java.util.List;

public interface ClientePlanificadorExterno {
  void enviarLote(Lote lote, List<Camion> camiones, String callbackUrl);
}