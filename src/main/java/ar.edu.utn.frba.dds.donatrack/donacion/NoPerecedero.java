package ar.edu.utn.frba.dds.donatrack.donacion;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;
public class NoPerecedero extends Bien {

  private Boolean usado;

  public NoPerecedero(String descripcion,
                      float cantidad,
                      UnidadMedida unidad,
                      byte[] foto,
                      Subcategoria subcategoria,
                      Boolean usado) {

    super(descripcion, cantidad, unidad, foto, subcategoria);
    this.usado = usado;
  }
}