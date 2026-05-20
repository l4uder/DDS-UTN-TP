package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Categoria;
import ar.edu.utn.frba.dds.donatrack.clasificacion.SegmentadorDonaciones;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donacion.Bien;
import ar.edu.utn.frba.dds.donatrack.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donacion.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.Perecedero;
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
    alimentos = new Categoria("Alimentos");
    mobiliario = new Categoria("Mobiliario");
    fideos = new Subcategoria("Fideos secos", alimentos);
    tomate = new Subcategoria("Tomate envasado", alimentos);
    sillas = new Subcategoria("Sillas de oficina", mobiliario);
    mesas = new Subcategoria("Mesas", mobiliario);
  }

  @Test
  void sePuedeSegmentarBienesDeDistintaSubcategoria() {
    Bien bienFideos = new Perecedero("Fideos", 100, UnidadMedida.UNIDADES, null, fideos, LocalDateTime.of(2027, 1, 1, 0, 0));
    Bien bienTomate = new Perecedero("Tomate", 50, UnidadMedida.UNIDADES, null, tomate, LocalDateTime.of(2027, 1, 1, 0, 0));

    RegistroEntrega registro = new RegistroEntrega("Donación planta de pastas", List.of(bienFideos, bienTomate));
    List<Donacion> donaciones = segmentador.segmentar(registro);

    assertEquals(2, donaciones.size());
  }

  @Test
  void sePuedeSegmentarPerecederosPorFechaDeVencimiento() {
    Bien fideos2027 = new Perecedero("Fideos", 100, UnidadMedida.UNIDADES, null, fideos, LocalDateTime.of(2027, 1, 1, 0, 0));
    Bien fideos2026 = new Perecedero("Fideos", 50, UnidadMedida.UNIDADES, null, fideos, LocalDateTime.of(2026, 6, 1, 0, 0));

    RegistroEntrega registro = new RegistroEntrega("Fideos con distintas fechas", List.of(fideos2027, fideos2026));
    List<Donacion> donaciones = segmentador.segmentar(registro);

    assertEquals(2, donaciones.size());
  }

  @Test
  void noSeSegmentanPerecederosConMismaFechaYSubcategoria() {
    Bien fideos1 = new Perecedero("Fideos", 100, UnidadMedida.UNIDADES, null, fideos, LocalDateTime.of(2027, 1, 1, 0, 0));
    Bien fideos2 = new Perecedero("Fideos", 200, UnidadMedida.UNIDADES, null, fideos, LocalDateTime.of(2027, 1, 1, 0, 0));

    RegistroEntrega registro = new RegistroEntrega("Fideos misma fecha", List.of(fideos1, fideos2));
    List<Donacion> donaciones = segmentador.segmentar(registro);

    assertEquals(1, donaciones.size());
  }

  @Test
  void sePuedeSegmentarNoPerecederosPorEstado() {
    Bien sillaUsada = new NoPerecedero("Silla", 6, UnidadMedida.UNIDADES, null, sillas, true);
    Bien sillaNueva = new NoPerecedero("Silla", 2, UnidadMedida.UNIDADES, null, sillas, false);

    RegistroEntrega registro = new RegistroEntrega("Sillas nuevas y usadas", List.of(sillaUsada, sillaNueva));
    List<Donacion> donaciones = segmentador.segmentar(registro);

    assertEquals(2, donaciones.size());
  }

  @Test
  void sePuedeSegmentarEjemploArcosPlatados() {
    Bien sillas = new NoPerecedero("Silla", 6, UnidadMedida.UNIDADES, null, this.sillas, true);
    Bien mesa = new NoPerecedero("Mesa rectangular", 1, UnidadMedida.UNIDADES, null, this.mesas, true);

    RegistroEntrega registro = new RegistroEntrega("Mudanza Arcos Plateados", List.of(sillas, mesa));
    List<Donacion> donaciones = segmentador.segmentar(registro);

    assertEquals(2, donaciones.size());
  }
}