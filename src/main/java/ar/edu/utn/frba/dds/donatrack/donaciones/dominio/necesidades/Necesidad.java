package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@Embeddable
public class Necesidad {
  @Setter
  @Column(name = "id_necesidad")
  private Long id;
  @Column(name = "descripcion")
  private String descripcion;
  @Column(name = "tipo_necesidad")
  @Enumerated(EnumType.STRING)
  private TipoNecesidad tipo;
  @ManyToOne
  @JoinColumn(name = "id_subcategoria")
  private Subcategoria subcategoria;
  @Column(name = "unidad_medida")
  @Enumerated (EnumType.STRING)
  private UnidadMedida unidadMedida;
  @Column(name = "cantidad_requerida")
  private Integer cantidadRequerida;
  @Column(name = "cantidad_recibida")
  private Integer cantidadRecibida;
  @Column(name = "frecuencia")
  @Enumerated(EnumType.STRING)
  private Frecuencia frecuencia;

  private Necesidad(String descripcion, TipoNecesidad tipo, Subcategoria subcategoria, UnidadMedida unidadMedida,
                   Integer cantidadRequerida, Frecuencia frecuencia) {
    checkDatosBase(descripcion, subcategoria, unidadMedida, cantidadRequerida);
    this.descripcion = descripcion;
    this.tipo = tipo;
    this.subcategoria = subcategoria;
    this.unidadMedida = unidadMedida;
    this.cantidadRecibida = 0;
    this.cantidadRequerida = cantidadRequerida;
    this.frecuencia = frecuencia;
  }

  private void checkDatosBase(String descripcion, Subcategoria subcategoria, UnidadMedida unidadMedida, Integer cantidadRequerida) {
    if (subcategoria == null || subcategoria.getNombre().isBlank()) {
      throw new DominioException("El campo 'subcategoria' es obligatorio");
    }
    if (unidadMedida == null) {
      throw new DominioException("El campo 'unidad_Medida' es obligatorio");
    }
    if (descripcion == null || descripcion.isBlank()) {
      throw new DominioException("El campo 'descripcion' es obligatorio");
    }
    if (cantidadRequerida == null || cantidadRequerida <= 0) {
      throw new DominioException("Una necesidad extraordinaria necesita 'cantidad_requerida' mayor a cero");
    }
  }

  public float getCantidadFaltanteEnMenorMedida() {
    return this.unidadMedida.convertirAMenorMedida(this.getCantidadFaltante());
  }

  public void recibirBienes(Integer cantidad) {
    this.cantidadRecibida += cantidad;
  }

  public Boolean estaSatisfecha() {
    return this.cantidadRecibida >= this.cantidadRequerida;
  }

  public static Necesidad crearNecesidadExtraordinaria(String descripcion, Subcategoria subcategoria, UnidadMedida unidadMedida, Integer cantidadRequerida) {
    return new Necesidad(descripcion, TipoNecesidad.EXTRAORDINARIA, subcategoria, unidadMedida, cantidadRequerida, null);
  }

  public static Necesidad crearNecesidadRecurrente( String descripcion, Subcategoria subcategoria, UnidadMedida unidadMedida, Integer cantidadRequerida, Frecuencia frecuencia) {
    if (frecuencia == null)
      throw new DominioException( "Una necesidad recurrente necesita 'periodo' puede ser: " + Arrays.toString(Frecuencia.values()));

    return new Necesidad(descripcion, TipoNecesidad.RECURRENTE, subcategoria, unidadMedida, cantidadRequerida, frecuencia);
  }

  private void actualizarDatosBase(String descripcion, Subcategoria subcategoria, UnidadMedida unidadMedida, Integer cantidadRequerida) {
    this.subcategoria = subcategoria;
    this.unidadMedida = unidadMedida;
    this.descripcion = descripcion;
    this.cantidadRequerida = cantidadRequerida;
  }

  public void actualizarDatosExtraordinaria(String descripcion, Subcategoria subcategoria, UnidadMedida unidadMedida, Integer cantidadRequerida) {
    checkDatosBase(descripcion, subcategoria, unidadMedida, cantidadRequerida);
    actualizarDatosBase(descripcion, subcategoria, unidadMedida, cantidadRequerida);
  }

  public void actualizarDatosRecurrente(String descripcion, Subcategoria subcategoria, UnidadMedida unidadMedida, Integer cantidadRequerida, Frecuencia frecuencia) {
    checkDatosBase(descripcion, subcategoria, unidadMedida, cantidadRequerida);
    if (frecuencia == null)
      throw new DominioException( "Una necesidad recurrente necesita 'periodo' puede ser: " + Arrays.toString(Frecuencia.values()));

    actualizarDatosBase(descripcion, subcategoria, unidadMedida, cantidadRequerida);
    this.frecuencia = frecuencia;
  }

  //==================== FUNCIONES AUXILIARES =======================
  private Integer getCantidadFaltante() {
    int cantidadFaltante = this.cantidadRequerida - this.cantidadRecibida;
    return Math.max(0, cantidadFaltante);
  }

}
