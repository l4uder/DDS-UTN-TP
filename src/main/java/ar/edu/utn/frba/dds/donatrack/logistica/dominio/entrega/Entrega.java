package ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entrega {

  private String id;
  private Beneficiario destino;
  private List<DonacionEnTransito> donaciones;
  private Camion camionAsignado;
  private List<EstadoEntrega> historialEstados;
  private List<String> fotosRecepcion;


  public Entrega(
      Beneficiario destino,
      List<DonacionEnTransito> donaciones,
      Camion camion
  ) {
    this.id = UUID.randomUUID().toString();
    this.destino = destino;
    this.donaciones = donaciones;
    this.camionAsignado = camion;
    this.historialEstados = new ArrayList<>();
    this.fotosRecepcion = new ArrayList<>();

    historialEstados.add(new EstadoEntrega(TipoEstadoEntrega.PENDIENTE, camion));
  }

  public void confirmarListaParaEntregar() {
    validarTransicionDesde(TipoEstadoEntrega.PENDIENTE, "confirmar como lista para entregar");
    cambiarEstado(TipoEstadoEntrega.LISTA_PARA_ENTREGAR,
        "Asignada a camión " + camionAsignado.getPatente());
  }

  public void iniciarTraslado() {
    validarTransicionDesde(TipoEstadoEntrega.LISTA_PARA_ENTREGAR, "iniciar traslado");
    cambiarEstado(TipoEstadoEntrega.EN_TRASLADO, "Iniciando recorrido");
  }

  public void confirmarRecepcion() {
    validarTransicionDesde(TipoEstadoEntrega.EN_TRASLADO, "confirmar recepción");
    cambiarEstado(TipoEstadoEntrega.ENTREGADA, null);
  }

  public void marcarNoRecibida(String motivo) {
    if (motivo == null || motivo.isBlank()) {
      throw new DomainValidationException("Debe indicar un motivo");
    }
    validarTransicionDesde(TipoEstadoEntrega.EN_TRASLADO, "marcar como no recibida");
    cambiarEstado(TipoEstadoEntrega.NO_RECIBIDA, motivo);
  }

  public void reingresarDeposito() {
    validarTransicionDesde(TipoEstadoEntrega.NO_RECIBIDA, "reingresar a depósito");
    cambiarEstado(TipoEstadoEntrega.PENDIENTE, "Entrega devuelta al depósito");
  }
  public void agregarFotoRecepcion(String url) {
    if (getEstadoActual() != TipoEstadoEntrega.ENTREGADA) {
      throw new IllegalStateException(
          "Solo se pueden cargar fotos de una entrega ya confirmada como entregada");
    }
    if (url == null || url.isBlank()) {
      throw new DomainValidationException("La URL de la foto es obligatoria");
    }
    fotosRecepcion.add(url);
  }

  public void reasignarCamion(Camion camion) {
    if (camion == null) {
      throw new DomainValidationException("El camión no puede ser nulo");
    }
    validarTransicionDesde(TipoEstadoEntrega.PENDIENTE, "reasignar camión");
    this.camionAsignado = camion;
  }

  private void cambiarEstado(TipoEstadoEntrega estado, String detalle) {
    historialEstados.add(new EstadoEntrega(estado, detalle, camionAsignado));
  }

  private void validarTransicionDesde(TipoEstadoEntrega esperado, String accion) {
    if (getEstadoActual() != esperado) {
      throw new IllegalStateException(
          "No se puede " + accion + " desde el estado " + getEstadoActual());
    }
  }

  public TipoEstadoEntrega getEstadoActual(){
    return historialEstados
        .get(historialEstados.size()-1)
        .getTipoEstado();
  }

  public String getId() {
    return id;
  }

  public List<DonacionEnTransito> getDonaciones() {
    return donaciones;
  }

  public Camion getCamionAsignado() {
    return camionAsignado;
  }

  public Beneficiario getDestino() {
    return destino;
  }

  public List<String> getFotosRecepcion() {
    return fotosRecepcion;
  }

  public List<EstadoEntrega> getHistorialEstados() {
    return historialEstados;
  }
}