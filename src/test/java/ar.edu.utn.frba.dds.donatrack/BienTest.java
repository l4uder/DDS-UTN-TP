package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.frba.dds.donatrack.donacion.Perecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

public class BienTest{
  @Test
  public void sePuedeCrearUnBienPerecedero() {

    Subcategoria pastas = new Subcategoria("pastas");

    Perecedero fideos = BienBuilder.bienPerecedero(
        "Fideos secos",
        LocalDateTime.of(2026,5,20,0,0),
        100,
        UnidadMedida.UNIDAD,
        null,
        pastas
    );

    assertEquals("Fideos secos", fideos.getDescripcion());
  }
  @Test
  public void sePuedeCrearSalsaDeTomate() {

    Subcategoria enlatados = new Subcategoria("enlatados");

    Perecedero salsaTomate = BienBuilder.bienPerecedero(
        "Salsa de tomate",
        LocalDateTime.of(2027,1,1,0,0),
        50,
        UnidadMedida.UNIDAD,
        null,
        enlatados
    );

    assertEquals("Salsa de tomate", salsaTomate.getDescripcion());
  }
  @Test
  public void sePuedeCrearSillasUsadas() {

    Subcategoria muebles = new Subcategoria("muebles");

    NoPerecedero sillas = BienBuilder.bienNoPerecedero(
        "Sillas de oficina",
        6,
        UnidadMedida.UNIDAD,
        null,
        muebles,
        true
    );

    assertEquals("Sillas de oficina", sillas.getDescripcion());
  }

  @Test
  public void sePuedeCrearMesaUsada() {

    Subcategoria muebles = new Subcategoria("muebles");

    NoPerecedero mesa = BienBuilder.bienNoPerecedero(
        "Mesa rectangular",
        1,
        UnidadMedida.UNIDAD,
        null,
        muebles,
        true
    );

    assertEquals("Mesa rectangular", mesa.getDescripcion());
  }
}
