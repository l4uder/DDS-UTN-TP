package ar.edu.utn.frba.dds.donatrack.donacion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Donacion {
  private UUID idDonacion;
  private String descripcion;
  private List<Bien> bienes;
  private EstadoDonacion estado;
  private List<HistorialEstado> historialEstados;

  public Donacion(List<Bien> bienes) {
    this.idDonacion = UUID.randomUUID();
    this.estado = EstadoDonacion.EN_DEPOSITO;
    this.bienes = new ArrayList<>(bienes);
    this.descripcion = this.descripcionGeneral(bienes);
    this.historialEstados = new ArrayList<>();
    this.historialEstados.add(new HistorialEstado(this.estado, "Donación segmentada"));
  }

  public String descripcionGeneral(List<Bien> bienes) {
    return bienes.stream()
        .map(b -> b.getCantidad() + " " + b.getUnidadMedida() + " de " + b.getDescripcion())
        .collect(Collectors.joining(", "));
  }

  public void cambiarEstado(EstadoDonacion nuevoEstado, String observacion) {
    if (nuevoEstado == EstadoDonacion.ENTREGA_FALLIDA &&
        (observacion == null || observacion.isBlank())) {
      throw new IllegalArgumentException(
          "Se requiere justificación para entrega fallida");
    }

    this.estado = nuevoEstado;
    this.historialEstados.add(new HistorialEstado(
        nuevoEstado,
        (observacion == null || observacion.isBlank()) ? null : observacion
    ));
  }
}