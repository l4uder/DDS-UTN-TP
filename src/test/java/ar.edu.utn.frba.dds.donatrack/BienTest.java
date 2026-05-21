package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Categoria;
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
    Categoria alimentos = new Categoria("Alimentos");
    Subcategoria pastas = new Subcategoria("pastas", alimentos);

    Perecedero fideos = BienBuilder.bienPerecedero(
        "Fideos secos",
        LocalDateTime.of(2026,5,20,0,0),
        100,
        UnidadMedida.UNIDADES,
        null,
        pastas
    );

    assertEquals("Fideos secos", fideos.getDescripcion());
  }
  @Test
  public void sePuedeCrearSalsaDeTomate() {
    Categoria alimentos = new Categoria("Alimentos");
    Subcategoria enlatados = new Subcategoria("enlatados", alimentos);

    Perecedero salsaTomate = BienBuilder.bienPerecedero(
        "Salsa de tomate",
        LocalDateTime.of(2027,1,1,0,0),
        50,
        UnidadMedida.UNIDADES,
        null,
        enlatados
    );

    assertEquals("Salsa de tomate", salsaTomate.getDescripcion());
  }
  @Test
  public void sePuedeCrearSillasUsadas() {
    Categoria mobiliario  = new Categoria("Mobiliario");
    Subcategoria muebles = new Subcategoria("muebles", mobiliario);

    NoPerecedero sillas = BienBuilder.bienNoPerecedero(
        "muebles de oficina",
        6,
        UnidadMedida.UNIDADES,
        null,
        muebles,
        true
    );

    assertEquals("Sillas de oficina", sillas.getDescripcion());
  }

  @Test
  public void sePuedeCrearMesaUsada() {
    Categoria muebles = new Categoria("muebles");
    Subcategoria mesaRectangular = new Subcategoria("mesa rectangular", muebles);

    NoPerecedero mesa = BienBuilder.bienNoPerecedero(
        "Mesa rectangular",
        1,
        UnidadMedida.UNIDADES,
        null,
        mesaRectangular,
        true
    );

    assertEquals("Mesa rectangular", mesa.getDescripcion());
  }
}
