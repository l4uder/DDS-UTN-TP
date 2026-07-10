package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Chofer;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.TipoEstadoEntrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.BeneficiarioDTO;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.DonacionAsignadaDTO;
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
  private BeneficiarioDTO beneficiario;

  @BeforeEach
  void setUp() {
    camion = new Camion("AB123CD", 10f, 2.5f, 1500f);
    chofer = new Chofer("Juan", "Gómez", "12345678");

    beneficiario = new BeneficiarioDTO("ben-1", "Comedor San José", "Av. Siempre Viva 123");

    DonacionAsignadaDTO donacion = new DonacionAsignadaDTO("don-1", "Fideos", beneficiario);

    entrega = new Entrega(beneficiario, List.of(donacion), camion);
    ruta = new Ruta(camion, LocalDate.now(), List.of(entrega));
  }

  @Test
  void unaRutaNuevaNoEstaIniciada() {
    assertFalse(ruta.isIniciada());
  }

  @Test
  void noSePuedeIniciarSinChofer() {
    assertThrows(IllegalStateException.class, () -> ruta.iniciarRecorrido());
  }

  @Test
  void asignarChoferPermiteIniciarRuta() {
    ruta.asignarChofer(chofer);

    ruta.iniciarRecorrido();

    assertTrue(ruta.isIniciada());
    assertEquals(TipoEstadoEntrega.EN_TRASLADO, entrega.getEstadoActual());
  }

  @Test
  void noSePuedeAsignarDosVecesElChofer() {
    ruta.asignarChofer(chofer);
    Chofer otroChofer = new Chofer("Pedro", "Díaz", "87654321");

    assertThrows(IllegalStateException.class, () -> ruta.asignarChofer(otroChofer));
  }

  @Test
  void noSePuedeIniciarUnaRutaDosVeces() {
    ruta.asignarChofer(chofer);
    ruta.iniciarRecorrido();

    assertThrows(IllegalStateException.class, () -> ruta.iniciarRecorrido());
  }
}
