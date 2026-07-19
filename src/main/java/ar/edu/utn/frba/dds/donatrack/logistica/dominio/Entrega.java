package ar.edu.utn.frba.dds.donatrack.logistica.dominio;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entrega {
  private String id;
  private Beneficiario destino;
  private List<Donacion> donaciones;
  private Camion camionAsignado;
  private List<EstadoEntrega> historialEstados;
  private List<String> fotosRecepcion;

  public Entrega(Beneficiario destino, List<Donacion> donaciones, Camion camion) {
    this.id = UUID.randomUUID().toString();//Todo: Porque este id lo ponemos nosotros y no un repo como en los demás casos ?
    this.destino = destino;
    this.donaciones = donaciones;
    this.camionAsignado = camion;
    this.historialEstados = new ArrayList<>();
    this.fotosRecepcion = new ArrayList<>();

    historialEstados.add(new EstadoEntrega(TipoEstadoEntrega.PENDIENTE, camion));
  }

  public void iniciarTraslado() {
    cambiarEstado(TipoEstadoEntrega.EN_TRASLADO,"Iniciando recorrido");
  }

  public void confirmarRecepcion() {
    cambiarEstado(TipoEstadoEntrega.ENTREGADA,null);
  }

  public void marcarNoRecibida(String motivo) {
    if (motivo == null || motivo.isBlank()) {
      throw new DomainValidationException("Debe indicar un motivo");
    }

    cambiarEstado(TipoEstadoEntrega.NO_RECIBIDA,motivo);
  }

  public void reingresarDeposito() {
    if (getEstadoActual() != TipoEstadoEntrega.NO_RECIBIDA) {
      throw new IllegalStateException("Solo una entrega no recibida puede volver al depósito");
    }

    cambiarEstado(TipoEstadoEntrega.PENDIENTE, "Entrega devuelta al depósito");
  }

  public void agregarFotoRecepcion(String url) {
    if(url == null || url.isBlank()) {
      throw new DomainValidationException("La URL de la foto es obligatoria");
    }

    fotosRecepcion.add(url);
  }

  private void cambiarEstado(TipoEstadoEntrega estado, String detalle){
    historialEstados.add(new EstadoEntrega(estado, detalle, camionAsignado));
  }

  public TipoEstadoEntrega getEstadoActual(){
    return historialEstados
        .get(historialEstados.size()-1)
        .getTipoEstado();
  }

  public void reasignarCamion(Camion camion) {
    if (camion == null) {
      throw new DomainValidationException("El camión no puede ser nulo");
    }

    this.camionAsignado = camion;
  }

  public void confirmarListaParaEntregar() {
    cambiarEstado(TipoEstadoEntrega.LISTA_PARA_ENTREGAR,
        "Asignada a camión " + camionAsignado.getPatente());
  }


  public String getId() {
    return id;
  }

  public List<Donacion> getDonaciones() {
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

  public boolean tieneFotos() {
    return !fotosRecepcion.isEmpty();
  }
}