package ar.edu.utn.frba.dds.donatrack.donacion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Donacion {
  private String descripcion;
  private List<Bien> bienes;
  private List<EstadoDonacion> historialEstados;

  public Donacion(String descripcion, List<Bien> bienes) {
    this.descripcion = (descripcion == null || descripcion.isBlank()) ? this.descripcionGeneral(bienes) : descripcion;
    this.bienes = bienes != null ? new ArrayList<>(bienes) : new ArrayList<>();
    this.historialEstados = new ArrayList<>();
    this.historialEstados.add(new EstadoDonacion(TipoEstadoDonacion.EN_DEPOSITO));
  }

  public Donacion(List<Bien> bienes) {
      this(null, bienes);
  }

  public TipoEstadoDonacion getEstadoActual() {
      return historialEstados.get(historialEstados.size() - 1).getTipoEstado();
  }

  public String descripcionGeneral(List<Bien> bienes) {
    return bienes.stream()
        .map(b -> b.getCantidad() + " " + b.getUnidadMedida() + " de " + b.getDescripcion())
        .collect(Collectors.joining(", "));
  }

  public void cambiarEstado(EstadoDonacion nuevoEstado) {
    //this.estado = nuevoEstado;
    this.historialEstados.add(nuevoEstado);
  }

  public List<TipoEstadoDonacion> getHistorialEstados() {
     return historialEstados.stream().map(EstadoDonacion::getTipoEstado).toList();
  }
}