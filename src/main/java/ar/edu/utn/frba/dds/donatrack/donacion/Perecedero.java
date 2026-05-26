package ar.edu.utn.frba.dds.donatrack.donacion;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Perecedero extends Bien {
  private LocalDateTime fechaVencimiento;

  public Perecedero(String descripcion, float cantidad,
                    UnidadMedida unidad, byte[] foto,
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