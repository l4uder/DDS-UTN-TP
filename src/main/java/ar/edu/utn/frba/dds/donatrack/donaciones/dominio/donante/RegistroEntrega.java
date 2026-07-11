package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
public class RegistroEntrega {
  private LocalDateTime fecha;
  private String descripcionGeneral;
  private List<Bien> bienes;
  @Getter
  private Donante donante;

  public RegistroEntrega(String descripcionGeneral, List<Bien> bienes, Donante donante) {
    this.fecha = LocalDateTime.now();
    this.donante = donante;
    this.descripcionGeneral = descripcionGeneral;
    this.bienes = new ArrayList<>(bienes);
  }

  public LocalDateTime getFecha() {
    return this.fecha;
  }

  public void agregarBien(Bien bien) {
    this.bienes.add(bien);
  }

  public List<Bien> getBienes() {
    return bienes;
  }
}