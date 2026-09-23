package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("extraordinaria")
public class NecesidadExtraordinaria extends Necesidad {

  public NecesidadExtraordinaria(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, Integer cantidadRequerida) {
    super(subcategoria, unidadMedida, descripcion, cantidadRequerida);
  }

  @Override
  public String getTipo() {
    return "EXTRAORDINARIA";
  }

  public void actualizarDatos(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, Integer cantidadRequerida) {
    super.actualizarDatosBase(subcategoria, unidadMedida, descripcion, cantidadRequerida);
  }

}
