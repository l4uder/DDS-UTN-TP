package ar.edu.utn.frba.dds.donatrack.logistica.web.integracion;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion.Lote;
import java.util.List;

public class ClientePlanificadorExternoMock implements ClientePlanificadorExterno {
  @Override
  public void enviarLote(Lote lote, List<Camion> camiones, String callbackUrl) {
    System.out.println("[MOCK] Enviando lote de " + lote.getEntregas().size()
        + " entregas (" + lote.cantidadDonaciones() + " donaciones) con "
        + camiones.size() + " camiones");
    System.out.println("[MOCK] Callback URL: " + callbackUrl);
  }
}