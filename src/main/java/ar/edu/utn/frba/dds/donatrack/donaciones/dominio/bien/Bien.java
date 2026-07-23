package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien.Perecedero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien.TipoBien;
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

  private Bien(String descripcion, float cantidad, UnidadMedida unidad,
               String foto, Subcategoria subcategoria, TipoBien tipoBien) {
    this.descripcion = descripcion;
    this.cantidad = cantidad;
    this.unidadMedida = unidad;
    this.foto = foto;
    this.subcategoria = subcategoria;
    this.tipoBien = tipoBien;
  }

  public String getNombreClave() {
    return this.tipoBien.getNombreClave(this.subcategoria);
  }

  public static Bien crearPerecedero(String descripcion, float cantidad, UnidadMedida unidad,
                                     String foto, Subcategoria subcategoria, LocalDate fechaVencimiento) {

    TipoBien tipoBien = new Perecedero(fechaVencimiento);
    return new Bien(descripcion, cantidad, unidad, foto, subcategoria, tipoBien);
  }

  public static Bien crearNoPerecedero(String descripcion, float cantidad, UnidadMedida unidad,
                                       String foto, Subcategoria subcategoria, Boolean usado) {

    TipoBien tipoBien = new NoPerecedero(usado);
    return new Bien(descripcion, cantidad, unidad, foto, subcategoria, tipoBien);
  }

  public double getCantidadMenorMedida() {
    return this.unidadMedida.convertirAMenorMedida(cantidad);
  }

}