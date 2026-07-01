package ar.edu.utn.frba.dds.donatrack.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.dominio.bien.Bien;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegistroEntrega {
  private LocalDateTime fecha;
  private String descripcionGeneral;
  private List<Bien> bienes;

  public RegistroEntrega(String descripcionGeneral, List<Bien> bienes) {
    this.fecha = LocalDateTime.now();
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