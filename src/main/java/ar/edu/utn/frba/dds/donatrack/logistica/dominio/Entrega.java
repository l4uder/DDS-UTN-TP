package ar.edu.utn.frba.dds.donatrack.logistica.dominio;

import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.BeneficiarioDTO;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.DonacionAsignadaDTO;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
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


  public Entrega(
      BeneficiarioDTO destino,
      List<DonacionAsignadaDTO> donaciones,
      Camion camion
  ) {
    this.id = UUID.randomUUID().toString();
    this.destino = destino;
    this.donaciones = donaciones;
    this.camionAsignado = camion;
    this.historialEstados = new ArrayList<>();
    this.fotosRecepcion = new ArrayList<>();

    historialEstados.add(
        new EstadoEntrega(
            TipoEstadoEntrega.PENDIENTE,
            camion
        )
    );
  }


  public void iniciarTraslado() {
    cambiarEstado(TipoEstadoEntrega.EN_TRASLADO,"Iniciando recorrido"
    );
  }


  public void confirmarRecepcion() {
    cambiarEstado(TipoEstadoEntrega.ENTREGADA,null);
  }


  public void marcarNoRecibida(String motivo) {

    if (motivo == null || motivo.isBlank()) {
      throw new DomainValidationException(
          "Debe indicar un motivo"
      );
    }

    cambiarEstado(TipoEstadoEntrega.NO_RECIBIDA,motivo);
  }


  public void reingresarDeposito() {

    if (getEstadoActual() != TipoEstadoEntrega.NO_RECIBIDA) {
      throw new IllegalStateException(
          "Solo una entrega no recibida puede volver al depósito"
      );
    }

    cambiarEstado(
        TipoEstadoEntrega.PENDIENTE,
        "Entrega devuelta al depósito"
    );
  }


  public void agregarFotoRecepcion(String url) {

    if(url == null || url.isBlank()) {
      throw new DomainValidationException(
          "La URL de la foto es obligatoria"
      );
    }

    fotosRecepcion.add(url);
  }


  private void cambiarEstado(
      TipoEstadoEntrega estado,
      String detalle
  ){
    historialEstados.add(
        new EstadoEntrega(
            estado,
            detalle,
            camionAsignado
        )
    );
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

  public List<DonacionAsignadaDTO> getDonaciones() {
    return donaciones;
  }

  public Camion getCamionAsignado() {
    return camionAsignado;
  }

  public BeneficiarioDTO getDestino() {
    return destino;
  }

  public List<String> getFotosRecepcion() {
    return fotosRecepcion;
  }

  public List<EstadoEntrega> getHistorialEstados() {
    return historialEstados;
  }
}