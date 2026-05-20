package ar.edu.utn.frba.dds.donatrack.donacion;

import java.time.LocalDateTime;

public class HistorialEstado {
  private LocalDateTime fecha;
  private EstadoDonacion estado;
  private String observacion;

  public HistorialEstado(EstadoDonacion estado, String observacion) {
    this.estado = estado;
    this.fecha = LocalDateTime.now();
    this.observacion = observacion;
  }
}