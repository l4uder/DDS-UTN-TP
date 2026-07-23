package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.frba.dds.donatrack.builder.CategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.SubcategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BienTest{
  //Categorias
  private Categoria alimentos;
  private Categoria muebles;
  //Subcategorias
  private Subcategoria pastas;
  private Subcategoria tetrapack;
  private Subcategoria sillas;
  private Subcategoria mesas;

  @BeforeEach
  void configuracionInicial() {
    //Categorias
    alimentos = new CategoriaBuilder().conNombre("Alimentos").build();
    muebles = new CategoriaBuilder().conNombre("Muebles").build();
    //Subcategorias
    pastas = new SubcategoriaBuilder().conNombre("Pastas").conCategoria(alimentos).build();
    tetrapack = new SubcategoriaBuilder().conNombre("Tetrapack").conCategoria(alimentos).build();
    sillas = new SubcategoriaBuilder().conNombre("Sillas").conCategoria(muebles).build();
    mesas = new SubcategoriaBuilder().conNombre("Mesas").conCategoria(muebles).build();
  }

  @Test
  public void sePuedeCrearUnBienPerecedero() {
    Bien fideos = new BienBuilder()
        .conDescripcion("Fideos secos")
        .conCantidad(10)
        .conSubcategoria(pastas)
        .conUnidad(UnidadMedida.UNIDADES)
        .conFechaVencimiento(LocalDate.now().plusMonths(6))
        .buildPerecedero();

    assertEquals("Fideos secos", fideos.getDescripcion());
  }
  @Test
  public void sePuedeCrearSalsaDeTomate() {
    Bien salsaTomate = new BienBuilder()
        .conDescripcion("Salsa de tomate")
        .conCantidad(2)
        .conSubcategoria(tetrapack)
        .conFechaVencimiento(LocalDate.of(2027, 1, 1))
        .conUnidad(UnidadMedida.UNIDADES)
        .buildPerecedero();

    assertEquals("Salsa de tomate", salsaTomate.getDescripcion());
  }
  @Test
  public void sePuedeCrearSillasUsadas() {
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
    Bien mesaRectancular = new BienBuilder()
        .conDescripcion("Mesa rectangular nueva")
        .conCantidad(3)
        .conSubcategoria(mesas)
        .conUsado(false)
        .buildNoPerecedero();

    assertEquals("Mesa rectangular nueva", mesaRectancular.getDescripcion());
  }
}
