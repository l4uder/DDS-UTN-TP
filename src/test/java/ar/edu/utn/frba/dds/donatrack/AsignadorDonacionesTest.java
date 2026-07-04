package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador.Asignador;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador.AlgoritmoMatchmaking;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AsignadorDonacionesTest {
  //los algoritmos
  private AlgoritmoMatchmaking algoritmoMock1;
  private AlgoritmoMatchmaking algoritmoMock2;
  //donaciones
  private Donacion donacion;
  //beneficiarios
  private Beneficiario beneficiario1;
  private Beneficiario beneficiario2;
  private Beneficiario beneficiario3;
  private Beneficiario beneficiario4;
  //asignador
  private Asignador asignador;

  @BeforeEach
  void configuracionIncial() {
    asignador = new Asignador();

    algoritmoMock1 = mock(AlgoritmoMatchmaking.class);
    algoritmoMock2 = mock(AlgoritmoMatchmaking.class);
    donacion = mock(Donacion.class);

    beneficiario1 = new Beneficiario("1111", "Direccion 1", List.of());
    beneficiario2 = new Beneficiario("2222", "Direccion 2", List.of());
    beneficiario3 = new Beneficiario("3333", "Direccion 3", List.of());
    beneficiario4 = new Beneficiario("4444", "Direccion 4", List.of());
  }

  @Test
  void siAmbosAlgoritmosConcuerdanDevolverEse() {
    asignador.agregarAlgoritmo(algoritmoMock1);
    asignador.agregarAlgoritmo(algoritmoMock2);

    List<Beneficiario> beneficiarios = List.of(beneficiario1, beneficiario2, beneficiario3);

    when(algoritmoMock1.generarRanking(donacion, beneficiarios)).thenReturn(List.of(beneficiario1, beneficiario2));
    when(algoritmoMock2.generarRanking(donacion, beneficiarios)).thenReturn(List.of(beneficiario2, beneficiario3));

    List<Beneficiario> resultado = asignador.asignar(donacion, beneficiarios);

    assertEquals(1, resultado.size());
    assertEquals(beneficiario2, resultado.get(0), "por que el beneficiario2, se encuentra en ambas listas");
  }

  @Test
  void siNoHayCoincidenciasDevolverTodosLosBeneficiarios() {
    asignador.agregarAlgoritmo(algoritmoMock1);
    asignador.agregarAlgoritmo(algoritmoMock2);

    List<Beneficiario> beneficiarios = List.of(beneficiario1, beneficiario2, beneficiario3, beneficiario4);

    when(algoritmoMock1.generarRanking(donacion, beneficiarios)).thenReturn(List.of(beneficiario1, beneficiario2));
    when(algoritmoMock2.generarRanking(donacion, beneficiarios)).thenReturn(List.of(beneficiario3, beneficiario4));

    List<Beneficiario> resultado = asignador.asignar(donacion, beneficiarios);

    assertEquals(4, resultado.size());
    assertEquals(beneficiarios, resultado);
  }

  @Test
  void siNoTieneAlgoritmosDebeDevolverUnaListaVacia() {
    List<Beneficiario> beneficiarios = List.of(beneficiario1, beneficiario2, beneficiario3);

    List<Beneficiario> resultado = asignador.asignar(donacion, beneficiarios);

    assertTrue(resultado.isEmpty());
  }
}
