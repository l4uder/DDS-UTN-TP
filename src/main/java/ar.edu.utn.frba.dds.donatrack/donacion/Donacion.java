package ar.edu.utn.frba.dds.donatrack.donacion;

import ar.edu.utn.frba.dds.donatrack.beneficiario.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.exception.DomainValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Donacion {
  private String descripcion;
  private List<Bien> bienes;
  private List<EstadoDonacion> historialEstados;
  private EntidadBeneficiaria entidadAsignada;

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
        .map(b -> b
        .getCantidad() + " " + b.getUnidadMedida() + " de " + b
        .getDescripcion())
        .collect(Collectors.joining(", "));
  }

  public void cambiarEstado(TipoEstadoDonacion nuevoEstado, String observacion) {
    if (nuevoEstado == TipoEstadoDonacion.ENTREGA_FALLIDA
        && (observacion == null || observacion.isBlank())) {
      throw new DomainValidationException("Se requiere justificación para entrega fallida");
    }
    this.historialEstados.add(new EstadoDonacion(nuevoEstado, observacion));
  }

  public List<EstadoDonacion> getHistorialEstados() {
    return new ArrayList<>(historialEstados);
  }

  public List<TipoEstadoDonacion> getTiposEstado() {
    return historialEstados.stream()
        .map(EstadoDonacion::getTipoEstado)
        .toList();
  }

  public void asignarA(EntidadBeneficiaria beneficiario) {
    this.entidadAsignada = beneficiario;
    cambiarEstado(TipoEstadoDonacion.ASIGNACION_REALIZADA,
        "Se realizó la asignación a " + beneficiario.getRazonSocial());
  }

  public Subcategoria getSubcategoria() {
    return this.bienes.get(0).getSubcategoria(); // todos tienen la misma
  }

  public List<Bien> getBienes() {
    return new ArrayList<>(bienes);
  }
}