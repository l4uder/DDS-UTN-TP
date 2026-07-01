package ar.edu.utn.frba.dds.donatrack.dominio.donacion;

import ar.edu.utn.frba.dds.donatrack.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Donacion {
  private String descripcion;
  private List<Bien> bienes;
  private List<EstadoDonacion> historialEstados;
  private Beneficiario beneficiario;

  public Donacion(List<Bien> bienes) {
    if (bienes == null || bienes.isEmpty()) {
      throw new DomainValidationException("Una donación debe tener al menos un bien");
    }
    this.descripcion = this.descripcionGeneral(bienes);
    this.bienes = new ArrayList<>(bienes);
    this.historialEstados = new ArrayList<>();
    this.historialEstados.add(new EstadoDonacion(TipoEstadoDonacion.EN_DEPOSITO));
  }

  public TipoEstadoDonacion getEstadoActual() {
    return historialEstados.get(historialEstados.size() - 1).getTipoEstado();
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
    this.beneficiario = beneficiario;
    this.historialEstados.add(new EstadoDonacion(
        TipoEstadoDonacion.ASIGNACION_REALIZADA,
        "Se realizó la asignación a " + beneficiario.getRazonSocial()
    ));
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

  public EntidadBeneficiaria getEntidadAsignada() {
    return this.entidadAsignada;
  }
}