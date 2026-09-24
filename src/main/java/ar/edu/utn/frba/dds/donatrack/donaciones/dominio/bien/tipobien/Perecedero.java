package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@DiscriminatorValue("perecedero")
@Getter
public class Perecedero extends Bien {
  @Column(name="fecha_de_vencimiento")
  private LocalDate fechaVencimiento;

  public Perecedero(String descripcion, Float cantidad, UnidadMedida unidad,
                    String foto, Subcategoria subcategoria, LocalDate fechaVencimiento) {
    super(descripcion, cantidad, unidad, foto, subcategoria);
    if (fechaVencimiento == null) throw new DominioException("El campo 'fecha_vencimiento' es obligatorio, en el Bien Perecedero");
    this.fechaVencimiento = fechaVencimiento;
  }

  @Override
  public String getNombreClave() {
    return getSubcategoria().getNombre() + "_" + this.fechaVencimiento.format(DateTimeFormatter.BASIC_ISO_DATE);
  }

  @Override
  public String getTipo() {
    return "PERECEDERO";
  }

}