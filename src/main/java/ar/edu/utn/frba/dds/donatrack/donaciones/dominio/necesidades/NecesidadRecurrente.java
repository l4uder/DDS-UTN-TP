package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.Arrays;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("recurrente")
public class NecesidadRecurrente extends Necesidad {
  @Column(name = "frecuencia")
  @Enumerated(EnumType.STRING)
  private Frecuencia frecuencia;

  public NecesidadRecurrente(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, Integer cantidadRequerida, Frecuencia frecuencia) {
    super(subcategoria, unidadMedida, descripcion, cantidadRequerida);
    checkDatos(frecuencia);
    this.frecuencia = frecuencia;
  }

  private void checkDatos(Frecuencia frecuencia) {
    if (frecuencia == null) {
      throw new DominioException( "Una necesidad recurrente necesita 'periodo' puede ser: " + Arrays.toString(Frecuencia.values()));
    }
  }

  @Override
  public String getTipo() {
    return "RECURRENTE";
  }

  public void actualizarDatos(Subcategoria subcategoria, UnidadMedida unidadMedida, String descripcion, Integer cantidadRequerida, Frecuencia frecuencia) {
    super.actualizarDatosBase(subcategoria, unidadMedida, descripcion, cantidadRequerida);
    checkDatos(frecuencia);
    this.frecuencia = frecuencia;
  }
  
}

