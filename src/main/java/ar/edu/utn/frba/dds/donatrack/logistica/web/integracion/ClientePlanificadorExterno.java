package ar.edu.utn.frba.dds.donatrack.logistica.web.integracion;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import java.util.List;

public interface ClientePlanificadorExterno {
  void enviarLote(List<Entrega> entregas, List<Camion> camiones, String callbackUrl);
}