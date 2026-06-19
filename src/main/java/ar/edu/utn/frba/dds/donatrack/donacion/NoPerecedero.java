package ar.edu.utn.frba.dds.donatrack.donacion;

public class NoPerecedero extends Bien {
  private Boolean usado;

  public NoPerecedero(String descripcion,
                      float cantidad,
                      UnidadMedida unidad,
                      String foto,
                      Subcategoria subcategoria,
                      Boolean usado) {

    super(descripcion, cantidad, unidad, foto, subcategoria);
    this.usado = usado;
  }

  public Boolean esUsado() {
    return this.usado;
  }

  public String getNombreClave(){
    return subcategoria.getNombre() + "_" + (usado ? "usado" : "nuevo");
  }
}