package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@DiscriminatorValue("no_perecedero")
@Getter
public class NoPerecedero extends Bien {
  @Column(name="esta_usado")
  private Boolean estaUsado;

  public NoPerecedero(String descripcion, Float cantidad, UnidadMedida unidad,
                      String foto, Subcategoria subcategoria, Boolean usado) {
    super(descripcion, cantidad, unidad, foto, subcategoria);
    if (usado == null) throw new DominioException("El campo 'usado' es obligatorio, en el Bien No Perecedero");
    this.estaUsado = usado;
  }

  @Override
  public String getNombreClave() {
    return getSubcategoria().getNombre() + "_" + (estaUsado ? "usado" : "nuevo");
  }

  @Override
  public String getTipo() {
    return "NO_PERECEDERO";
  }

}