package ar.edu.utn.frba.dds.donatrack.logistica.dominio;

import java.util.List;

public class ClientePlanificadorExternoMock implements ClientePlanificadorExterno {
  @Override
  public void enviarLote(List<Entrega> entregas, List<Camion> camiones, String callbackUrl) {
    System.out.println("[MOCK] Enviando lote de " + entregas.size()
        + " entregas con " + camiones.size() + " camiones");
    System.out.println("[MOCK] Callback URL: " + callbackUrl);
  }
}