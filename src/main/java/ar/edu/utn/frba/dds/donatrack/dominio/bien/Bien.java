package ar.edu.utn.frba.dds.donatrack.dominio.bien;

import ar.edu.utn.frba.dds.donatrack.dominio.bien.tipoBien.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.dominio.bien.tipoBien.Perecedero;
import ar.edu.utn.frba.dds.donatrack.dominio.bien.tipoBien.TipoBien;
import java.time.LocalDateTime;

public class Bien {
  private String descripcion;
  private float cantidad;
  private UnidadMedida unidad;
  private String foto;
  private Subcategoria subcategoria;
  private TipoBien tipoBien;

  private Bien(String descripcion, float cantidad,
              UnidadMedida unidad, String foto,
              Subcategoria subcategoria, TipoBien tipoBien) {

    this.descripcion = descripcion;
    this.cantidad = cantidad;
    this.unidad = unidad;
    this.foto = foto;
    this.subcategoria = subcategoria;
    this.tipoBien = tipoBien;
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public float getCantidad() {
    return this.cantidad;
  }

  public UnidadMedida getUnidadMedida() {
    return this.unidad;
  }

  public Subcategoria getSubcategoria() {
    return this.subcategoria;
  }

  public String getNombreClave(){
    return this.tipoBien.getNombreClave(this.subcategoria);
  }

  public static Bien crearPerecedero(String descripcion, float cantidad, UnidadMedida unidad,
                                     String foto, Subcategoria subcategoria, LocalDateTime fechaVencimiento) {

    TipoBien tipoBien = new Perecedero(fechaVencimiento);
    return new Bien(descripcion, cantidad, unidad, foto, subcategoria, tipoBien);
  }

  public static Bien crearNoPerecedero(String descripcion, float cantidad, UnidadMedida unidad,
                                       String foto, Subcategoria subcategoria, Boolean usado) {

    TipoBien tipoBien = new NoPerecedero(usado);
    return new Bien(descripcion, cantidad, unidad, foto, subcategoria, tipoBien);
  }
}