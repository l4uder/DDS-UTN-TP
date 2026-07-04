package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ProveedorClienteCorreo;
import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.CategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.RegistroEntregaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.SubcategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.segmentador.SegmentadorDonaciones;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.RegistroEntrega;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SegmentadorDonacionesTest {
  //Segmentador
  private SegmentadorDonaciones segmentador;
  //Categorias
  private Categoria CategoriasAlimentos;
  private Categoria CategoriasMobiliario;
  //SubCategorias
  private Subcategoria SubcategoriaFideos;
  private Subcategoria SubcategoriaTomate;
  private Subcategoria SubcategoriaSillas;
  private Subcategoria SubcategoriaMesas;
  //Bienes
  private BienBuilder builtomate;
  private BienBuilder buildFideo;
  private BienBuilder buildSilla;
  private BienBuilder buildMesa;
  //Registros
  private RegistroEntregaBuilder buildRegistro;

  @BeforeEach
  void configuracionIncial() {
    //Segmentador
    segmentador = new SegmentadorDonaciones();
    //Categorias
    CategoriasAlimentos = new CategoriaBuilder().conNombre("Alimentos").build();
    CategoriasMobiliario = new CategoriaBuilder().conNombre("Mobiliario").build();
    //SubCategorias
    SubcategoriaTomate = new SubcategoriaBuilder().conNombre("Tomate envasado").conCategoria(CategoriasAlimentos).build();
    SubcategoriaFideos = new SubcategoriaBuilder().conNombre("Fideos secos").conCategoria(CategoriasAlimentos).build();
    SubcategoriaSillas = new SubcategoriaBuilder().conNombre("Sillas de oficina").conCategoria(CategoriasMobiliario).build();
    SubcategoriaMesas = new SubcategoriaBuilder().conNombre("Mesas").conCategoria(CategoriasMobiliario).build();
    //Bienes
    builtomate = new BienBuilder()
        .conDescripcion("tomate").conCantidad(1).conSubcategoria(SubcategoriaTomate)
        .conFechaVencimiento(LocalDate.of(2027, 1, 1))
        .conUnidad(UnidadMedida.SIN_UNIDAD);
    buildFideo = new BienBuilder()
        .conDescripcion("Fideos").conCantidad(100).conSubcategoria(SubcategoriaFideos)
        .conFechaVencimiento(LocalDate.of(2027, 1, 1))
        .conUnidad(UnidadMedida.SIN_UNIDAD);
    buildSilla = new BienBuilder()
        .conDescripcion("Silla").conCantidad(6).conSubcategoria(SubcategoriaSillas)
        .conUsado(true);
    buildMesa = new BienBuilder()
        .conDescripcion("Mesa rectangular").conCantidad(1).conSubcategoria(SubcategoriaMesas)
        .conUsado(true);
    //Registros
    buildRegistro = new RegistroEntregaBuilder();

    //Mock de Motor de correos exclusivo para los tests
    ProveedorClienteCorreo.inicializar((destino, mensaje) -> {
        // No hace nada de red, solo simula que lo envió
        System.out.println("TEST - Simulando envío a: " + destino);
    });
  }

  @Test
  void sePuedeSegmentarBienesDeDistintaSubcategoria() {
    Bien tomate = builtomate.conSubcategoria(SubcategoriaTomate).buildPerecedero();
    Bien fideo = buildFideo.conSubcategoria(SubcategoriaFideos).buildPerecedero();

    RegistroEntrega registro = buildRegistro.conDescripcion("Donación planta de pastas")
        .conBien(tomate).conBien(fideo).build();

    List<Donacion> donaciones = segmentador.segmentar(List.of(registro));

    assertEquals(2, donaciones.size());
    verificarConsistenciaDeSubcategoria(donaciones);
  }

  @Test
  void sePuedeSegmentarPerecederosPorFechaDeVencimiento() {
    Bien fideos2027 = buildFideo.conFechaVencimiento(LocalDate.of(2027, 1, 1)).buildPerecedero();
    Bien fideos2026 = buildFideo.conFechaVencimiento(LocalDate.of(2026, 6, 1)).buildPerecedero();

    RegistroEntrega registro = buildRegistro.conDescripcion("Fideos con distintas fechas")
        .conBien(fideos2027).conBien(fideos2026).build();

    List<Donacion> donaciones = segmentador.segmentar(List.of(registro));

    assertEquals(2, donaciones.size());
    verificarConsistenciaDeSubcategoria(donaciones);
  }

  @Test
  void noSeSegmentanPerecederoConMismaFechaYSubcategoria() {
    Bien fideos1 = buildFideo.conSubcategoria(SubcategoriaFideos)
        .conFechaVencimiento(LocalDate.of(2027, 1, 1))
        .buildPerecedero();

    Bien fideos2 = buildFideo.conSubcategoria(SubcategoriaFideos)
        .conFechaVencimiento(LocalDate.of(2027, 1, 1))
        .buildPerecedero();

    RegistroEntrega registro = buildRegistro.conDescripcion("Fideos misma fecha")
        .conBien(fideos1).conBien(fideos2).build();

    List<Donacion> donaciones = segmentador.segmentar(List.of(registro));

    assertEquals(1, donaciones.size());
    verificarConsistenciaDeSubcategoria(donaciones);
  }

  @Test
  void sePuedeSegmentarNoPerecederosPorEstado() {
    Bien sillaUsada = buildSilla.conUsado(true).buildNoPerecedero();
    Bien sillaNueva = buildSilla.conUsado(false).buildNoPerecedero();

    RegistroEntrega registro = buildRegistro.conDescripcion("Sillas nuevas y usadas")
        .conBien(sillaUsada).conBien(sillaNueva).build();

    List<Donacion> donaciones = segmentador.segmentar(List.of(registro));

    assertEquals(2, donaciones.size());
    verificarConsistenciaDeSubcategoria(donaciones);
  }

  @Test
  void sePuedeSegmentarEjemploArcosPlatados() {
    Bien sillaUsada = buildSilla.buildNoPerecedero();
    Bien mesaUsada = buildMesa.buildNoPerecedero();

    RegistroEntrega registro = buildRegistro.conDescripcion("Mudanza Arcos Plateados")
        .conBien(sillaUsada).conBien(mesaUsada).build();

    List<Donacion> donaciones = segmentador.segmentar(List.of(registro));

    assertEquals(2, donaciones.size());
    verificarConsistenciaDeSubcategoria(donaciones);
  }

  private void verificarConsistenciaDeSubcategoria(List<Donacion> donaciones) {
    donaciones.forEach(donacion ->
        donacion.getBienes().forEach(bien -> assertEquals(donacion.getSubcategoria(), bien.getSubcategoria()))
    );
  }

}