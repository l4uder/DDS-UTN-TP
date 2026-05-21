package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.CategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.RegistroEntregaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.SubcategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Categoria;
import ar.edu.utn.frba.dds.donatrack.clasificacion.SegmentadorDonaciones;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donacion.Bien;
import ar.edu.utn.frba.dds.donatrack.donacion.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donante.RegistroEntrega;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SegmentadorDonacionesTest {

  private SegmentadorDonaciones segmentador;
  private Categoria alimentos;
  private Categoria mobiliario;
  private Subcategoria fideos;
  private Subcategoria tomate;
  private Subcategoria sillas;
  private Subcategoria mesas;

  @BeforeEach
  void setUp() {
    segmentador = new SegmentadorDonaciones();
    alimentos = new CategoriaBuilder().conNombre("Alimentos").build();
    mobiliario = new CategoriaBuilder().conNombre("Mobiliario").build();
    fideos = new SubcategoriaBuilder().conNombre("Fideos secos").conCategoria(alimentos).build();
    tomate = new SubcategoriaBuilder().conNombre("Tomate envasado").conCategoria(alimentos).build();
    sillas = new SubcategoriaBuilder().conNombre("Sillas de oficina").conCategoria(mobiliario).build();
    mesas = new SubcategoriaBuilder().conNombre("Mesas").conCategoria(mobiliario).build();
  }

  @Test
  void sePuedeSegmentarBienesDeDistintaSubcategoria() {
    Bien bienFideos = new BienBuilder()
        .conDescripcion("Fideos").conCantidad(100).conSubcategoria(fideos)
        .conFechaVencimiento(LocalDateTime.of(2027, 1, 1, 0, 0))
        .buildPerecedero(UnidadMedida.UNIDADES);
    Bien bienTomate = new BienBuilder()
        .conDescripcion("Tomate").conCantidad(50).conSubcategoria(tomate)
        .conFechaVencimiento(LocalDateTime.of(2027, 1, 1, 0, 0))
        .buildPerecedero(UnidadMedida.UNIDADES);

    RegistroEntrega registro = new RegistroEntregaBuilder()
        .conDescripcion("Donación planta de pastas")
        .conBien(bienFideos).conBien(bienTomate)
        .build();

    assertEquals(2, segmentador.segmentar(List.of(registro)).size());
  }

  @Test
  void sePuedeSegmentarPerecederosPorFechaDeVencimiento() {
    Bien fideos2027 = new BienBuilder()
        .conDescripcion("Fideos").conCantidad(100).conSubcategoria(fideos)
        .conFechaVencimiento(LocalDateTime.of(2027, 1, 1, 0, 0))
        .buildPerecedero(UnidadMedida.UNIDADES);
    Bien fideos2026 = new BienBuilder()
        .conDescripcion("Fideos").conCantidad(50).conSubcategoria(fideos)
        .conFechaVencimiento(LocalDateTime.of(2026, 6, 1, 0, 0))
        .buildPerecedero(UnidadMedida.UNIDADES);

    RegistroEntrega registro = new RegistroEntregaBuilder()
        .conDescripcion("Fideos con distintas fechas")
        .conBien(fideos2027).conBien(fideos2026)
        .build();

    assertEquals(2, segmentador.segmentar(List.of(registro)).size());
  }

  @Test
  void noSeSegmentanPerecederoConMismaFechaYSubcategoria() {
    Bien fideos1 = new BienBuilder()
        .conDescripcion("Fideos").conCantidad(100).conSubcategoria(fideos)
        .conFechaVencimiento(LocalDateTime.of(2027, 1, 1, 0, 0))
        .buildPerecedero(UnidadMedida.UNIDADES);
    Bien fideos2 = new BienBuilder()
        .conDescripcion("Fideos").conCantidad(200).conSubcategoria(fideos)
        .conFechaVencimiento(LocalDateTime.of(2027, 1, 1, 0, 0))
        .buildPerecedero(UnidadMedida.UNIDADES);

    RegistroEntrega registro = new RegistroEntregaBuilder()
        .conDescripcion("Fideos misma fecha")
        .conBien(fideos1).conBien(fideos2)
        .build();

    assertEquals(1, segmentador.segmentar(List.of(registro)).size());
  }

  @Test
  void sePuedeSegmentarNoPerecederosPorEstado() {
    Bien sillaUsada = new BienBuilder()
        .conDescripcion("Silla").conCantidad(6).conSubcategoria(sillas)
        .usado().buildNoPerecedero();
    Bien sillaNueva = new BienBuilder()
        .conDescripcion("Silla").conCantidad(2).conSubcategoria(sillas)
        .buildNoPerecedero();

    RegistroEntrega registro = new RegistroEntregaBuilder()
        .conDescripcion("Sillas nuevas y usadas")
        .conBien(sillaUsada).conBien(sillaNueva)
        .build();

    assertEquals(2, segmentador.segmentar(List.of(registro)).size());
  }

  @Test
  void sePuedeSegmentarEjemploArcosPlatados() {
    Bien sillaUsada = new BienBuilder()
        .conDescripcion("Silla").conCantidad(6).conSubcategoria(sillas)
        .usado().buildNoPerecedero();
    Bien mesaUsada = new BienBuilder()
        .conDescripcion("Mesa rectangular").conCantidad(1).conSubcategoria(mesas)
        .usado().buildNoPerecedero();

    RegistroEntrega registro = new RegistroEntregaBuilder()
        .conDescripcion("Mudanza Arcos Plateados")
        .conBien(sillaUsada).conBien(mesaUsada)
        .build();

    assertEquals(2, segmentador.segmentar(List.of(registro)).size());
  }
}