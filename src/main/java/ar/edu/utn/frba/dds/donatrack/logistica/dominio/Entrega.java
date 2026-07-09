package ar.edu.utn.frba.dds.donatrack.logistica.dominio;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import java.util.ArrayList;
import java.util.List;

public class Entrega {
  private Beneficiario destino;
  private List<Donacion> donaciones;
  private Camion camionAsignado;
  private List<EstadoEntrega> historialEstados;
  private List<String> fotosRecepcion;

  public Entrega(Beneficiario destino, List<Donacion> donaciones, Camion camion) {
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

  //ver si es mejor ese nombre o cambiar por confirmarRuta
  public void confirmarListaParaEntregar() {
    this.donaciones.forEach(d -> d.confirmarRuta());
  }

  public void iniciarTraslado() {
    this.cambiarEstado(TipoEstadoEntrega.EN_TRASLADO, "Iniciando recorrido");
    this.donaciones.forEach(d -> d.confirmarTrasladoEnCurso());
    this.donaciones.
  }

  public void confirmarRecepcion() {
    this.cambiarEstado(TipoEstadoEntrega.ENTREGADA, null);
    this.donaciones.forEach(d -> d.confirmarEntrega());
  }

  public void marcarNoRecibida(String motivo) {
    this.cambiarEstado(TipoEstadoEntrega.NO_RECIBIDA, motivo);
    this.donaciones.forEach(d -> d.notificarEntregaFallida(motivo));
  }

  public void reingresarDeposito() {
    if (getEstadoActual() != TipoEstadoEntrega.NO_RECIBIDA) {
      throw new IllegalStateException("Solo puede reingresar al depósito una entrega No recibida");
    }
    this.cambiarEstado(TipoEstadoEntrega.PENDIENTE, "Donación reingresada al depósito");
    this.donaciones.forEach(d -> d.confirmarRecepcionDeposito());
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

  public Beneficiario getDestino() {
    return destino;
  }

  public List<Donacion> getDonaciones() {
    return donaciones;
  }

  public Camion getCamionAsignado() {
    return camionAsignado;
  }

  public List<String> getFotosRecepcion() {
    return fotosRecepcion;
  }
}
