package ar.edu.utn.frba.dds.donatrack.logistica.dominio;

import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.BeneficiarioDTO;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.DonacionAsignadaDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entrega {
  private String id;
  private BeneficiarioDTO destino;
  private List<DonacionAsignadaDTO> donaciones;
  private Camion camionAsignado;
  private List<EstadoEntrega> historialEstados;
  private List<String> fotosRecepcion;

  public Entrega(BeneficiarioDTO destino, List<DonacionAsignadaDTO> donaciones, Camion camion) {
    this.id = UUID.randomUUID().toString();
    this.destino = destino;
    this.donaciones = donaciones;
    this.camionAsignado = camion;
    this.historialEstados = new ArrayList<>();
    historialEstados.add(new EstadoEntrega(TipoEstadoEntrega.PENDIENTE, camion));
    this.fotosRecepcion = new ArrayList<>();
  }

  private void cambiarEstado(TipoEstadoEntrega estado, String observacion) {
    this.historialEstados
            .add(new EstadoEntrega(estado, observacion, this.camionAsignado));
  }

  public void confirmarListaParaEntregar() {
    // La propagación del estado a donaciones se realiza en GestorRuta vía DonacionesClient.
  }

  public void iniciarTraslado() {
    this.cambiarEstado(TipoEstadoEntrega.EN_TRASLADO, "Iniciando recorrido");
  }

  public void confirmarRecepcion() {
    this.cambiarEstado(TipoEstadoEntrega.ENTREGADA, null);
  }

  public void marcarNoRecibida(String motivo) {
    this.cambiarEstado(TipoEstadoEntrega.NO_RECIBIDA, motivo);
  }

  public void reingresarDeposito() {
    if (getEstadoActual() != TipoEstadoEntrega.NO_RECIBIDA) {
      throw new IllegalStateException("Solo puede reingresar al depósito una entrega No recibida");
    }
    this.cambiarEstado(TipoEstadoEntrega.PENDIENTE, "Donación reingresada al depósito");
  }

  public void reasignarCamion(Camion nuevoCamion) {
    this.camionAsignado = nuevoCamion;
  }

  public void agregarFotoRecepcion(String urlFoto) {
    if (urlFoto != null && !urlFoto.isBlank()) {
      this.fotosRecepcion.add(urlFoto);
    }
  }

  public boolean tieneFotos() {
    return !fotosRecepcion.isEmpty();
  }

  public TipoEstadoEntrega getEstadoActual() {
    return historialEstados.get(historialEstados.size() - 1).getTipoEstado();
  }

  public List<EstadoEntrega> getHistorialEstados() {
    return historialEstados;
  }

  public String getId() {
    return id;
  }

  public BeneficiarioDTO getDestino() {
    return destino;
  }

  public List<DonacionAsignadaDTO> getDonaciones() {
    return donaciones;
  }

  public Camion getCamionAsignado() {
    return camionAsignado;
  }

  public List<String> getFotosRecepcion() {
    return fotosRecepcion;
  }

  public void setId(String id) {
    this.id = id;
  }
}
