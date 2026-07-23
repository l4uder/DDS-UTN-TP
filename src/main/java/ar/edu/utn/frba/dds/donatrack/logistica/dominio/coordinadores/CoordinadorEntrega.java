package ar.edu.utn.frba.dds.donatrack.logistica.dominio.coordinadores;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.DonacionesClient;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoEntregadaRequest;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoErrorEntregaRequest;

public class CoordinadorEntrega {
  private final EntregaRepository repository;
  private final DonacionesClient donacionesClient;

  public CoordinadorEntrega(EntregaRepository repository, DonacionesClient donacionesClient){
    this.repository = repository;
    this.donacionesClient = donacionesClient;
  }


  public void confirmarRecepcion(String id){
    Entrega entrega =repository.buscarPorId(id);
    entrega.confirmarRecepcion();

    entrega.getDonaciones().forEach(d ->
            donacionesClient.cambiarEstadoDonacion(
                d.getId(), new CambioEstadoEntregadaRequest(entrega.getCamionAsignado()
                        .getPatente()
                )
            )
        );

    repository.guardar(entrega);
  }



  public void marcarNoRecibida(String id, String motivo){
    Entrega entrega = repository.buscarPorId(id);

    entrega.marcarNoRecibida(motivo);
    entrega.getDonaciones().forEach(d ->
            donacionesClient.cambiarEstadoDonacion(d.getId(),new CambioEstadoErrorEntregaRequest(
                    motivo
                )
            )
        );

    repository.guardar(entrega);
  }

  public void reingresarDeposito(String id){
    Entrega entrega = repository.buscarPorId(id);
    entrega.reingresarDeposito();

    entrega.getDonaciones().forEach(d ->
        donacionesClient.cambiarEstadoDonacionVueltaDeposito(d.getId())
    );

    repository.eliminar(id);
  }
}