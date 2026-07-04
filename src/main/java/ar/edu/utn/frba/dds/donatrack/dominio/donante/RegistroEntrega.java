package ar.edu.utn.frba.dds.donatrack.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.Donante;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegistroEntrega {
  private LocalDateTime fecha;
  private String descripcionGeneral;
  private List<Bien> bienes;
  private Donante donante;

  public RegistroEntrega(Donante donante, String descripcionGeneral, List<Bien> bienes) {
    this.donante = donante;
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

  public Donante getDonante(){
    return this.donante;
  }
}