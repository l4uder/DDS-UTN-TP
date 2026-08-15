package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.entrega;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

@Getter
public class RegistroEntrega {
  private LocalDateTime fecha;
  private String descripcionGeneral;
  private List<Bien> bienes;
  private Donante donante; //Doble Referencia

  public RegistroEntrega(String descripcionGeneral, List<Bien> bienes, Donante donante) {
    this.fecha = LocalDateTime.now();
    this.donante = donante;
    this.descripcionGeneral = descripcionGeneral;
    this.bienes = new ArrayList<>(bienes);
  }

}