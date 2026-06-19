package ar.edu.utn.frba.dds.donatrack.donacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Perecedero implements TipoBien {
  private LocalDateTime fechaVencimiento;

  public Perecedero(LocalDateTime fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }

  public LocalDateTime getFechaVencimiento() {
    return this.fechaVencimiento;
  }

  @Override
  public String getNombreClave(Subcategoria subcategoria){
    return subcategoria.getNombre() + "_" + this.fechaVencimiento.format(DateTimeFormatter.BASIC_ISO_DATE);
  }
}