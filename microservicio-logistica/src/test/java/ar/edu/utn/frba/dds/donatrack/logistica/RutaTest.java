package ar.edu.utn.frba.dds.donatrack.logistica;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta.Chofer;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.TipoEstadoEntrega;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class RutaTest {
  private Camion camion;
  private Chofer chofer;
  private Entrega entrega;
  private Ruta ruta;
  private Beneficiario beneficiario;


  @BeforeEach
  void setUp() {
    camion = new Camion("AB123CD", 10f, 2.5f, 1500f);
    chofer = new Chofer("Juan", "Gómez", "12345678");

    beneficiario = new Beneficiario(
        1L, "Comedor San José", "Av. Siempre Viva 123");

    DonacionEnTransito donacion = new DonacionEnTransito(1L, "Fideos", beneficiario);

    entrega = new Entrega(List.of(donacion), camion);
    entrega.confirmarListaParaEntregar();

    ruta = new Ruta(camion, LocalDate.now(), List.of(entrega));
  }

  @Test
  void unaRutaNuevaNoEstaIniciada() {
    assertFalse(ruta.isEstaIniciada());
  }

  @Test
  void noSePuedeIniciarSinChofer() {
    assertThrows(DominioException.class, () -> ruta.iniciarRecorrido());
  }

  @Test
  void asignarChoferPermiteIniciarRuta() {
    ruta.asignarChofer(chofer);

    ruta.iniciarRecorrido();

    assertTrue(ruta.isEstaIniciada());
    assertEquals(TipoEstadoEntrega.EN_TRASLADO, entrega.getEstadoActual());
  }

  @Test
  void noSePuedeAsignarDosVecesElChofer() {
    ruta.asignarChofer(chofer);
    Chofer otroChofer = new Chofer("Pedro", "Díaz", "87654321");

    assertThrows(DominioException.class, () -> ruta.asignarChofer(otroChofer));
  }

  @Test
  void noSePuedeIniciarUnaRutaDosVeces() {
    ruta.asignarChofer(chofer);
    ruta.iniciarRecorrido();

    assertThrows(DominioException.class, () -> ruta.iniciarRecorrido());
  }
}
