package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donacion.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.Perecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;

import java.time.LocalDateTime;

public class BienBuilder {

  public static Perecedero bienPerecedero(
      String descripcion,
      LocalDateTime fechaDeVencimiento,
      float cantidad,
      UnidadMedida unidad,
      byte[] foto,
      Subcategoria subcategoria
  ) {

    return new Perecedero(
        descripcion,
        cantidad,
        unidad,
        foto,
        subcategoria,
        fechaDeVencimiento
    );
  }

  public static NoPerecedero bienNoPerecedero(
      String descripcion,
      float cantidad,
      UnidadMedida unidad,
      byte[] foto,
      Subcategoria subcategoria,
      Boolean usado
  ) {

    return new NoPerecedero(
        descripcion,
        cantidad,
        unidad,
        foto,
        subcategoria,
        usado
    );
  }
}