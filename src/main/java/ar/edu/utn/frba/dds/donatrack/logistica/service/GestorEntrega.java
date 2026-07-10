package ar.edu.utn.frba.dds.donatrack.logistica.service;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.integracion.DonacionesClient;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;

import java.util.List;

public class GestorEntrega {
  private static final String ESTADO_ENTREGADA = "ENTREGADA";
  private static final String ESTADO_ENTREGA_FALLIDA = "ENTREGA_FALLIDA";
  private static final String ESTADO_EN_DEPOSITO = "EN_DEPOSITO";

  private final EntregaRepository entregaRepository;
  private final DonacionesClient donacionesClient;

  public GestorEntrega(EntregaRepository entregaRepository, DonacionesClient donacionesClient) {
    this.entregaRepository = entregaRepository;
    this.donacionesClient = donacionesClient;
  }

  public List<Entrega> listar() {
    return entregaRepository.buscarTodas();
  }

  public Entrega obtener(String id) {
    return entregaRepository.buscarPorId(id);
  }

  public void confirmarRecepcion(String id) {
    Entrega entrega = entregaRepository.buscarPorId(id);
    entrega.confirmarRecepcion();
    propagarEstadoDonaciones(entrega, ESTADO_ENTREGADA);
    entregaRepository.guardar(entrega);
  }

  public void marcarNoRecibida(String id, String motivo) {
    if (motivo == null || motivo.isBlank()) {
      throw new DomainValidationException("Se requiere un motivo para marcar como no recibida");
    }
    Entrega entrega = entregaRepository.buscarPorId(id);
    entrega.marcarNoRecibida(motivo);
    propagarEstadoDonaciones(entrega, ESTADO_ENTREGA_FALLIDA);
    entregaRepository.guardar(entrega);
  }

  public void reingresarADeposito(String id) {
    Entrega entrega = entregaRepository.buscarPorId(id);
    entrega.reingresarDeposito();
    propagarEstadoDonaciones(entrega, ESTADO_EN_DEPOSITO);
    entregaRepository.guardar(entrega);
  }

  public void agregarFoto(String id, String urlFoto) {
    if (urlFoto == null || urlFoto.isBlank()) {
      throw new DomainValidationException("La URL de la foto no puede estar vacía");
    }
    Entrega entrega = entregaRepository.buscarPorId(id);
    entrega.agregarFotoRecepcion(urlFoto);
    entregaRepository.guardar(entrega);
  }

  private void propagarEstadoDonaciones(Entrega entrega, String estado) {
    entrega.getDonaciones().forEach(d ->
        donacionesClient.cambiarEstadoDonacion(d.getId(), estado));
  }
}
