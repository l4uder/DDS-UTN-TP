package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien.Perecedero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien.TipoBien;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Bien {
  private String descripcion;
  private float cantidad;
  private UnidadMedida unidadMedida;
  private String foto;
  private Subcategoria subcategoria;
  private TipoBien tipoBien;

  private Bien(String descripcion, Float cantidad, UnidadMedida unidad,
               String foto, Subcategoria subcategoria, TipoBien tipoBien) {
    checkDatos(unidad, cantidad, subcategoria);
    this.descripcion = descripcion;
    this.cantidad = cantidad;
    this.unidadMedida = unidad;
    this.foto = foto;
    this.subcategoria = subcategoria;
    this.tipoBien = tipoBien;
  }

  private void checkDatos(UnidadMedida unidadMedida, Float cantidad, Subcategoria subcategoria) {
    if (unidadMedida == null)
      throw new DominioException("El campo 'unidad_medida' es obligatorio, en el Bien");

    if (cantidad == null || cantidad < 0.0)
      throw new DominioException("El campo 'cantidad' es obligatorio y debe ser positivo, en el Bien");

    if (subcategoria == null)
      throw new DominioException("El campo 'subcategoria' es obligatorio, en el Bien");
  }

  public String getNombreClave() {
    return this.tipoBien.getNombreClave(this.subcategoria);
  }

  public float getCantidadMenorMedida() {
    return this.unidadMedida.convertirAMenorMedida(cantidad);
  }

  public static Bien crearPerecedero(String descripcion, Float cantidad, UnidadMedida unidad,
                                     String foto, Subcategoria subcategoria, LocalDate fechaVencimiento) {

    TipoBien tipoBien = new Perecedero(fechaVencimiento);
    return new Bien(descripcion, cantidad, unidad, foto, subcategoria, tipoBien);
  }

  public static Bien crearNoPerecedero(String descripcion, Float cantidad, UnidadMedida unidad,
                                       String foto, Subcategoria subcategoria, Boolean usado) {

    TipoBien tipoBien = new NoPerecedero(usado);
    return new Bien(descripcion, cantidad, unidad, foto, subcategoria, tipoBien);
  }

}