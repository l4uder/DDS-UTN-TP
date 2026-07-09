package ar.edu.utn.frba.dds.donatrack.logistica.service;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;

import java.util.List;

public class GestorEntrega {
  private final EntregaRepository entregaRepository;

  public GestorEntrega(EntregaRepository entregaRepository) {
    this.entregaRepository = entregaRepository;
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
    entregaRepository.guardar(entrega);
  }

  public void marcarNoRecibida(String id, String motivo) {
    if (motivo == null || motivo.isBlank()) {
      throw new DomainValidationException("Se requiere un motivo para marcar como no recibida");
    }
    Entrega entrega = entregaRepository.buscarPorId(id);
    entrega.marcarNoRecibida(motivo);
    entregaRepository.guardar(entrega);
  }

  public void reingresarADeposito(String id) {
    Entrega entrega = entregaRepository.buscarPorId(id);
    entrega.reingresarDeposito();
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
}