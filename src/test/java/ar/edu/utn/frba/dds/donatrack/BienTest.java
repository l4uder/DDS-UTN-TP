package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.frba.dds.donatrack.builder.CategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.SubcategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

public class BienTest{
  private BienBuilder bien;
  private Categoria alimentos;
  private Categoria muebles;

  @BeforeEach
  void setUp() {
    bien = new BienBuilder();
    alimentos = new CategoriaBuilder().conNombre("Alimentos").build();
    muebles = new CategoriaBuilder().conNombre("Muebles").build();
  }

  @Test
  public void sePuedeCrearUnBienPerecedero() {
    Subcategoria pastas = new SubcategoriaBuilder()
        .conNombre("Pastas")
        .conCategoria(alimentos)
        .build();

    Bien fideos = new BienBuilder()
        .conDescripcion("Fideos secos")
        .conCantidad(10)
        .conSubcategoria(pastas)
        .conUnidad(UnidadMedida.UNIDADES)
        .buildPerecedero();

    assertEquals("Fideos secos", fideos.getDescripcion());
  }
  @Test
  public void sePuedeCrearSalsaDeTomate() {
    Subcategoria tetrapack = new SubcategoriaBuilder()
        .conNombre("Tetrapack")
        .conCategoria(alimentos)
        .build();

    Bien salsaTomate = new BienBuilder()
        .conDescripcion("Salsa de tomate")
        .conCantidad(2)
        .conSubcategoria(tetrapack)
        .conFechaVencimiento(LocalDateTime.of(2027, 1, 1, 0, 0))
        .conUnidad(UnidadMedida.UNIDADES)
        .buildPerecedero();

    assertEquals("Salsa de tomate", salsaTomate.getDescripcion());
  }
  @Test
  public void sePuedeCrearSillasUsadas() {
    Subcategoria sillas = new SubcategoriaBuilder()
        .conNombre("Sillas")
        .conCategoria(muebles).build();

    Bien sillasUsadas = new BienBuilder()
        .conDescripcion("Sillas de oficina usadas")
        .conCantidad(10)
        .conSubcategoria(sillas)
        .conUsado(true)
        .buildNoPerecedero();

    assertEquals("Sillas de oficina usadas", sillasUsadas.getDescripcion());
  }

  @Test
  public void sePuedeCrearMesa() {
    Subcategoria mesas = new SubcategoriaBuilder()
        .conNombre("Mesas")
        .conCategoria(muebles).build();

    Bien mesaRectancular = new BienBuilder()
        .conDescripcion("Mesa rectangular nueva")
        .conCantidad(3)
        .conSubcategoria(mesas)
        .buildNoPerecedero();

    assertEquals("Mesa rectangular nueva", mesaRectancular.getDescripcion());
  }
}
