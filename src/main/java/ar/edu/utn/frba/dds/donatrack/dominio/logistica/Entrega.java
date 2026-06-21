package ar.edu.utn.frba.dds.donatrack.dominio.logistica;

import ar.edu.utn.frba.dds.donatrack.dominio.beneficiario.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.donatrack.dominio.donacion.Donacion;
import java.util.ArrayList;
import java.util.List;

public class Entrega {
  private EntidadBeneficiaria destino;
  private List<Donacion> donaciones;
  private Camion camionAsignado;
  private List<CambioEstadoEntrega> historialEstados;
  private List<String> fotosRecepcion;

  public Entrega(EntidadBeneficiaria destino, List<Donacion> donaciones,
                 Camion camion) {
    this.destino = destino;
    this.donaciones = donaciones;
    this.camionAsignado = camion;
    this.historialEstados = new ArrayList<>();
    this.historialEstados.
        add(new CambioEstadoEntrega(this, TipoEstadoEntrega.PENDIENTE, camion));
    this.fotosRecepcion = new ArrayList<>();
  }

  public void cambiarEstado(TipoEstadoEntrega estado, String observacion) {
    this.historialEstados.
        add(new CambioEstadoEntrega(this, estado, observacion, this.camionAsignado));
  }

  public void iniciarTraslado() {
    this.cambiarEstado(TipoEstadoEntrega.EN_TRASLADO, "Inicio de recorrido");
    for (Donacion d : donaciones) {
      d.confirmarTrasladoEnCurso();
    }
  }

  public void confirmarRecepcion() {
    if (getEstadoActual() != TipoEstadoEntrega.EN_TRASLADO) {
      throw new IllegalStateException("Solo se puede confirmar una entrega En traslado");
    }
    this.cambiarEstado(TipoEstadoEntrega.ENTREGADA, null);
    for (Donacion d : donaciones) {
      d.confirmarEntrega();
    }
  }

  public void marcarNoRecibida(String motivo) {
    this.cambiarEstado(TipoEstadoEntrega.NO_RECIBIDA, motivo);
    for (Donacion d : donaciones) {
      d.notificarEntregaFallida(motivo);
    }
  }

  public void reingresarADeposito() {
    if (getEstadoActual() != TipoEstadoEntrega.NO_RECIBIDA) {
      throw new IllegalStateException("Solo puede reingresar al depósito una entrega No recibida");
    }
    this.cambiarEstado(TipoEstadoEntrega.PENDIENTE, "Donación reingresada al depósito");
    for (Donacion d : donaciones) {
      d.confirmarRecepcionDeposito();
    }
  }

  public void confirmarListaParaEntregar() {
    for (Donacion d : donaciones) {
      d.confirmarRuta();
    }
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

  public EntidadBeneficiaria getDestino() {
    return destino;
  }

  public List<Donacion> getDonaciones() {
    return donaciones;
  }

  public Camion getCamionAsignado() {
    return camionAsignado;
  }

  public List<CambioEstadoEntrega> getHistorialEstados() {
    return historialEstados;
  }

  public List<String> getFotosRecepcion() {
    return fotosRecepcion;
  }
}
