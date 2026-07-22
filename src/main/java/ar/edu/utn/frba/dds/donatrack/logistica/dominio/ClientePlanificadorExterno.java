package ar.edu.utn.frba.dds.donatrack.logistica.dominio;

import java.util.List;

public interface ClientePlanificadorExterno {
  void enviarLote(List<Entrega> entregas, List<Camion> camiones, String callbackUrl);
}