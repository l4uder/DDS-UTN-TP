package ar.edu.utn.frba.dds.donatrack.donacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Perecedero extends Bien {
  private LocalDateTime fechaVencimiento;

  public Perecedero(String descripcion, float cantidad,
                    UnidadMedida unidad, String foto,
                    Subcategoria subcategoria, LocalDateTime fechaVencimiento) {

    super(descripcion, cantidad, unidad, foto, subcategoria);
    this.fechaVencimiento = fechaVencimiento;
  }

  public LocalDateTime getFechaVencimiento() {
    return this.fechaVencimiento;
  }

  public String getNombreClave(){
    return subcategoria.getNombre() + "_" + fechaVencimiento.format(DateTimeFormatter.BASIC_ISO_DATE);
  }
}