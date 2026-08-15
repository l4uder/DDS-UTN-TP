package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class Perecedero implements TipoBien {
  private LocalDate fechaVencimiento;

  public Perecedero(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }

  @Override
  public String getNombreClave(Subcategoria subcategoria) {
    return subcategoria.getNombre() + "_"
        + this.fechaVencimiento.format(DateTimeFormatter.BASIC_ISO_DATE);
  }

}