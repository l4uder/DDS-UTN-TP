package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ProveedorClienteCorreo;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.CategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.SubcategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.algoritmos.CompatibilidadSemantica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.algoritmos.PrioridadSubAtendidos;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.NecesidadExtraordinaria;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AlgoritmoMatchmakingTest {
  //Categorias
  private Categoria alimentos;
  private Categoria muebles;
  private Categoria ropa;
  //SubCategorias
  Subcategoria frutas;
  Subcategoria lacteos;
  Subcategoria sillas;
  Subcategoria remera;
  //Bienes
  Bien manzanasRojas;
  Bien yogurDream;
  Bien sillaMadera;
  Bien remeraMangaCorta;
  //Donacion
  private Donacion donacion1;
  private Donacion donacionAsignada1;
  private Donacion donacionAsignada2;
  private Donacion donacionAsignada3;
  //MedioContacto
  CorreoDeContato correo;
  //Beneficiario
  private Beneficiario beneficiario1;
  private Beneficiario beneficiario2;
  private Beneficiario beneficiario3;
  private Beneficiario beneficiario4;
  private Beneficiario beneficiario5;
  private Beneficiario beneficiario6;
  private Beneficiario beneficiario7;
  private Beneficiario beneficiario8;
  private Beneficiario beneficiario9;
  private Beneficiario beneficiario10;
  private Beneficiario beneficiario11;
  private Beneficiario beneficiario12;
  //Algoritmos match
  CompatibilidadSemantica compatibilidadSemantica;
  PrioridadSubAtendidos prioridadASubAtendidos;
  //Donante
  private Donante donantePrueba;

  @BeforeEach
  void configuracionInicial() {
    //Mock de Motor de correos exclusivo para los tests
    ProveedorClienteCorreo.inicializar((destino, mensaje) -> {
        // No hace nada de red, solo simula que lo envió
        System.out.println("TEST - Simulando envío a: " + destino);
    });

    //Categorias
    alimentos = new CategoriaBuilder().conNombre("Alimentos").build();
    muebles = new CategoriaBuilder().conNombre("Muebles").build();
    ropa = new CategoriaBuilder().conNombre("Ropa").build();
    //SubCategorias
    frutas = new SubcategoriaBuilder().conNombre("frutas").conCategoria(alimentos).build();
    lacteos = new SubcategoriaBuilder().conNombre("verduras").conCategoria(alimentos).build();
    sillas = new SubcategoriaBuilder().conNombre("Sillas").conCategoria(muebles).build();
    remera = new SubcategoriaBuilder().conNombre("Remera").conCategoria(ropa).build();
    //Bienes
    manzanasRojas = new BienBuilder()
        .conDescripcion("Manzanas Rojas")
        .conCantidad(10)
        .conSubcategoria(frutas)
        .conUnidad(UnidadMedida.KILOGRAMOS)
        .conFechaVencimiento(LocalDate.now().plusMonths(1))
        .buildPerecedero();
    yogurDream = new BienBuilder()
        .conDescripcion("yogur con nueces")
        .conCantidad(3)
        .conSubcategoria(lacteos)
        .conUnidad(UnidadMedida.LITROS)
        .conFechaVencimiento(LocalDate.now().plusMonths(3))
        .buildPerecedero();
    sillaMadera = new BienBuilder()
        .conDescripcion("silla de madera nueva")
        .conCantidad(3)
        .conSubcategoria(sillas)
        .conUsado(false)
        .buildNoPerecedero();
    remeraMangaCorta = new BienBuilder()
        .conDescripcion("Remera manga corta nueva")
        .conCantidad(1)
        .conSubcategoria(remera)
        .conUsado(false)
        .buildNoPerecedero();
    //Donacion
    //donacion = new Donacion(List.of(fideos));
    //MedioContacto
    correo = new CorreoDeContato("correo@gmail.com");
    //Beneficiario
    beneficiario1 = new Beneficiario("4444", "av. Varela 1800", List.of(correo));
    beneficiario2 = new Beneficiario("5555", "av. Irigoyen 88", List.of(correo));
    beneficiario3 = new Beneficiario("6666", "Rivadavia 1000", List.of(correo));
    beneficiario4 = new Beneficiario("6666", "Rivadavia 1000", List.of(correo));
    beneficiario5 = new Beneficiario("6666", "Rivadavia 1000", List.of(correo));
    beneficiario6 = new Beneficiario("6666", "Rivadavia 1000", List.of(correo));
    beneficiario7 = new Beneficiario("6666", "Rivadavia 1000", List.of(correo));
    beneficiario8 = new Beneficiario("6666", "Rivadavia 1000", List.of(correo));
    beneficiario9 = new Beneficiario("6666", "Rivadavia 1000", List.of(correo));
    beneficiario10 = new Beneficiario("6666", "Rivadavia 1000", List.of(correo));
    beneficiario11 = new Beneficiario("6666", "Rivadavia 1000", List.of(correo));
    beneficiario12 = new Beneficiario("6666", "Rivadavia 1000", List.of(correo));
    //Algoritmos match
    compatibilidadSemantica = new CompatibilidadSemantica();
    prioridadASubAtendidos = new PrioridadSubAtendidos();
    //Donante
    donantePrueba = new PersonaHumanaBuilder()
      .conNombre("Juan")
      .conApellido("Pérez")
      .conDocumento(new Documento(TipoDocumento.DNI, "12345678"))
      .conContactoPrincipal(new CorreoDeContato("juan@prueba.com"))
      .build();
  }

  @Test
  void debeQuitarBeneficiariosSinCoincidencias() {
    NecesidadExtraordinaria necesitaRemeras = new NecesidadExtraordinaria(remera, "....", 2);
    necesitaRemeras.setUnidadMedida(UnidadMedida.SIN_UNIDAD);
    beneficiario1.agregarNecesidad(necesitaRemeras);
    NecesidadExtraordinaria necesitaSillas = new NecesidadExtraordinaria(sillas, "....", 3);
    necesitaSillas.setUnidadMedida(UnidadMedida.SIN_UNIDAD);
    beneficiario2.agregarNecesidad(necesitaSillas);
    NecesidadExtraordinaria necesitaFrutas = new NecesidadExtraordinaria(frutas, "....", 3);
    necesitaFrutas.setUnidadMedida(UnidadMedida.KILOGRAMOS);
    beneficiario3.agregarNecesidad(necesitaFrutas);

    donacion1 = new Donacion(List.of(yogurDream, manzanasRojas), List.of(donantePrueba));

    List<Beneficiario> beneficiarios = compatibilidadSemantica.generarRanking(donacion1, List.of(beneficiario1, beneficiario2, beneficiario3));

    assertEquals(1, beneficiarios.size());
    assertEquals(beneficiario3, beneficiarios.get(0), "es al único beneficiario que le sirve la donación");
  }

  @Test
  void debePriorizarAlBeneficiariosQueMasLeConviene() {
    NecesidadExtraordinaria necesita2KGFrutas = new NecesidadExtraordinaria(frutas, "....", 2);
    necesita2KGFrutas.setUnidadMedida(UnidadMedida.KILOGRAMOS);
    beneficiario1.agregarNecesidad(necesita2KGFrutas);
    NecesidadExtraordinaria necesitaLacteos = new NecesidadExtraordinaria(lacteos, "....", 1);
    necesitaLacteos.setUnidadMedida(UnidadMedida.LITROS);
    beneficiario1.agregarNecesidad(necesitaLacteos);
    NecesidadExtraordinaria necesita3KGFrutas = new NecesidadExtraordinaria(frutas, "....", 3);
    necesita3KGFrutas.setUnidadMedida(UnidadMedida.KILOGRAMOS);
    beneficiario2.agregarNecesidad(necesita3KGFrutas);

    donacion1 = new Donacion(List.of(yogurDream, manzanasRojas), List.of(donantePrueba));

    List<Beneficiario> beneficiarios = compatibilidadSemantica.generarRanking(donacion1, List.of(beneficiario1, beneficiario2, beneficiario3));

    assertEquals(2, beneficiarios.size());
    assertEquals(beneficiario1, beneficiarios.get(0), "debe ser el primero ya que le sirve tanto el yogur como las manzanas");
    assertEquals(beneficiario2, beneficiarios.get(1), "debe ser el segundo ya que le sirve solo las manzanas");
  }
/*
  @Test
  void debeLimitarElResultadoADiezBeneficiarios() {
    beneficiario1.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));
    beneficiario2.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));
    beneficiario3.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));
    beneficiario4.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));
    beneficiario5.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));
    beneficiario6.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));
    beneficiario7.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));
    beneficiario8.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));
    beneficiario9.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));
    beneficiario10.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));
    beneficiario11.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));
    beneficiario12.agregarNecesidad(new NecesidadExtraordinaria(frutas, "....", 2));

    donacion1 = new Donacion(List.of(yogurDream, manzanasRojas), List.of(donantePrueba));

    List<Beneficiario> beneficiarios = compatibilidadSemantica.generarRanking(donacion1, List.of(beneficiario1, beneficiario2, beneficiario3, beneficiario4, beneficiario5, beneficiario6, beneficiario7, beneficiario8, beneficiario9, beneficiario10, beneficiario11, beneficiario12));
    assertEquals(10, beneficiarios.size());
  }
*/
  @Test
  void debePriorizarAlBeneficiarioConMenosDonaciones() {
    //benefeciario1 recibe una donacion
    donacionAsignada1 = new Donacion(List.of(sillaMadera), List.of(donantePrueba));
    donacionAsignada1.confirmarAsignacion(beneficiario1);
    //beneficiario2 recibe dos donaciones
    donacionAsignada2 = new Donacion(List.of(yogurDream), List.of(donantePrueba));
    donacionAsignada3 = new Donacion(List.of(remeraMangaCorta), List.of(donantePrueba));
    donacionAsignada2.confirmarAsignacion(beneficiario2);
    donacionAsignada3.confirmarAsignacion(beneficiario2);

    donacion1 = new Donacion(List.of(yogurDream, manzanasRojas), List.of(donantePrueba));

    List<Beneficiario> beneficiarios = prioridadASubAtendidos.generarRanking(donacion1, List.of(beneficiario1, beneficiario2, beneficiario3));

    assertEquals(beneficiario3, beneficiarios.get(0), "porque no tiene donaciones");
    assertEquals(beneficiario1, beneficiarios.get(1), "porque tiene 1 donación");
    assertEquals(beneficiario2, beneficiarios.get(2), "porque tiene 2 donaciones");
  }

  @Test
  void debeIgnorarLasDonacionesAnterioresATresMeses() {
    //benefeciario1 recibe una donacion
    donacionAsignada1 = new Donacion(List.of(sillaMadera), List.of(donantePrueba));
    donacionAsignada1.confirmarAsignacion(beneficiario1);
    //beneficiario2 recibe dos donaciones pero el año pasado
    donacionAsignada2 = new Donacion(List.of(yogurDream), List.of(donantePrueba));
    donacionAsignada3 = new Donacion(List.of(remeraMangaCorta), List.of(donantePrueba));
    donacionAsignada2.confirmarAsignacion(beneficiario2);
    donacionAsignada3.confirmarAsignacion(beneficiario2);
    donacionAsignada2.setFechaAsignacion(LocalDateTime.now().minusYears(1));
    donacionAsignada3.setFechaAsignacion(LocalDateTime.now().minusYears(1));

    donacion1 = new Donacion(List.of(yogurDream, manzanasRojas), List.of(donantePrueba));

    List<Beneficiario> beneficiarios = prioridadASubAtendidos.generarRanking(donacion1, List.of(beneficiario1, beneficiario2, beneficiario3));

    assertEquals(beneficiario3, beneficiarios.get(0), "porque no tiene donaciones");
    assertEquals(beneficiario2, beneficiarios.get(1), "porque tiene 0 donaciones en el ultimo trimestre");
    assertEquals(beneficiario1, beneficiarios.get(2), "porque tiene 1 donación en el ultimo trimestre");
  }
}
