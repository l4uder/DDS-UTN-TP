package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Donacion {
  private String descripcion;
  private List<Bien> bienes;
  private List<EstadoDonacion> historialEstados;
  //Doble asociacion bidericcional
  private Beneficiario beneficiario;
  private Donante donante;

  public Donacion(Donante donante, List<Bien> bienes) {
    if (bienes == null || bienes.isEmpty()) {
      throw new DomainValidationException("Una donación debe tener al menos un bien");
    }
    this.descripcion = this.descripcionGeneral(bienes);
    this.bienes = new ArrayList<>(bienes);
    this.historialEstados = new ArrayList<>();
    this.historialEstados.add(new EstadoDonacion(TipoEstadoDonacion.EN_DEPOSITO));
    this.donante = donante;
  }

  public Donante getDonante(){
    return this.donante;
  }

  public TipoEstadoDonacion getEstadoActual() {
    return historialEstados.get(historialEstados.size() - 1).getTipoEstado();
  }

  public LocalDateTime getFechaAsignacion() {
    return this.historialEstados.stream()
        .filter(e -> e.getTipoEstado() == TipoEstadoDonacion.ASIGNACION_REALIZADA)
        .findFirst()
        .map(EstadoDonacion::getFecha)
        .orElseThrow(() -> new DomainValidationException("Donacion no posee fecha de asignacion"));
  }

  /*Solo para poder probar un test despues ver como mejorar quitando esto*/
  public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
    EstadoDonacion estadoAsignacion = this.historialEstados.stream()
        .filter(e -> e.getTipoEstado() == TipoEstadoDonacion.ASIGNACION_REALIZADA)
        .findFirst()
        .orElseThrow(() -> new DomainValidationException("Donacion no posee fecha de asignacion"));

    estadoAsignacion.setFecha(fechaAsignacion);
  }

  public String descripcionGeneral(List<Bien> bienes) {
    return bienes.stream()
        .map(b -> b.getCantidad() + " " + b.getUnidadMedida() + " de " + b.getDescripcion())
        .collect(Collectors.joining(", "));
  }

  public void notificarEntregaFallida(String observacion) {
    if (observacion == null || observacion.isBlank()) {
      throw new DomainValidationException(
          "Se requiere justificación para notificar entrega fallida"
      );
    }
    if (getEstadoActual() == TipoEstadoDonacion.ENTREGA_FALLIDA) {
      return;
    }
    if (getEstadoActual() != TipoEstadoDonacion.EN_TRASLADO) {
      throw new CambioDeEstadoNoPermitidoException(
          "La donacion debe estar en traslado para notificar entrega fallida"
      );
    }
    this.historialEstados.add(
        new EstadoDonacion(TipoEstadoDonacion.ENTREGA_FALLIDA, observacion)
    );
  }

  public void confirmarEntrega() {
    if (getEstadoActual() == TipoEstadoDonacion.ENTREGADA) {
      return;
    }
    if (getEstadoActual() != TipoEstadoDonacion.EN_TRASLADO) {
      throw new CambioDeEstadoNoPermitidoException(
          "La donacion debe estar en traslado para confirmar entrega"
      );
    }
    this.historialEstados.add(new EstadoDonacion(TipoEstadoDonacion.ENTREGADA));
  }

  public void confirmarTrasladoEnCurso() {
    if (getEstadoActual() == TipoEstadoDonacion.EN_TRASLADO) {
      return;
    }
    if (getEstadoActual() != TipoEstadoDonacion.LISTA_PARA_ENTREGAR) {
      throw new CambioDeEstadoNoPermitidoException(
          "La donacion debe estar lista para entregar para iniciar el traslado"
      );
    }
    this.historialEstados.add(new EstadoDonacion(TipoEstadoDonacion.EN_TRASLADO));
  }

  public void confirmarRuta() {
    if (getEstadoActual() == TipoEstadoDonacion.LISTA_PARA_ENTREGAR) {
      return;
    }
    if (getEstadoActual() != TipoEstadoDonacion.ASIGNACION_REALIZADA) {
      throw new CambioDeEstadoNoPermitidoException(
          "La donacion debe estar asignada para confirmar ruta"
      );
    }
    this.historialEstados.add(new EstadoDonacion(TipoEstadoDonacion.LISTA_PARA_ENTREGAR));
  }

  public void confirmarAsignacion(Beneficiario beneficiario) {
    if (getEstadoActual() == TipoEstadoDonacion.ASIGNACION_REALIZADA) {
      return;
    }
    if (getEstadoActual() != TipoEstadoDonacion.EN_DEPOSITO) {
      throw new CambioDeEstadoNoPermitidoException(
          "No se puede asignar donacion a menos que este en deposito"
      );
    }
    this.historialEstados.add(new EstadoDonacion(
        TipoEstadoDonacion.ASIGNACION_REALIZADA,
        "Se realizó la asignación a " + beneficiario.getRazonSocial()
    ));
    this.beneficiario = beneficiario;
    beneficiario.asignarDonacion(this);

    String mensajeBeneficiario = "Se le ha asignado una nueva donación: " + this.descripcion;
    beneficiario.getContactoPrincipal().notificar(mensajeBeneficiario);

    String mensajeDonante = "Tu donación ha sido asignada a la entidad: " + beneficiario.getRazonSocial();
    this.donante.getContactoPrincipal().notificar(mensajeDonante);
  }

  public void marcarVencida() {
    if (getEstadoActual() == TipoEstadoDonacion.VENCIDA) {
      return;
    }
    if (getEstadoActual() != TipoEstadoDonacion.EN_DEPOSITO) {
      throw new CambioDeEstadoNoPermitidoException(
          "No se puede marcar como vencida si no esta en deposito"
      );
    }
    this.historialEstados.add(new EstadoDonacion(TipoEstadoDonacion.VENCIDA));
  }

  public void confirmarRecepcionDeposito() {
    if (getEstadoActual() == TipoEstadoDonacion.EN_DEPOSITO) {
      return;
    }
    if (getEstadoActual() != TipoEstadoDonacion.ENTREGA_FALLIDA) {
      throw new CambioDeEstadoNoPermitidoException(
          "No se puede recivir en deposito a menos que la entrega fracase"
      );
    }
    this.historialEstados.add(new EstadoDonacion(TipoEstadoDonacion.EN_DEPOSITO));
  }

  public List<EstadoDonacion> getHistorialEstados() {
    return new ArrayList<>(historialEstados);
  }

  public Subcategoria getSubcategoria() {
    return this.bienes.get(0).getSubcategoria(); // todos tienen la misma
  }

  public List<Bien> getBienes() {
    return new ArrayList<>(bienes);
  }

  public Beneficiario getBeneficiario() {
    return this.beneficiario;
  }
}