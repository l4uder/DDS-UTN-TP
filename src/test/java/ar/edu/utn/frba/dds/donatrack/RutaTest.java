package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.EntidadBeneficiariaBuilder;
import ar.edu.utn.frba.dds.donatrack.dominio.beneficiario.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.donatrack.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Camion;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Chofer;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Entrega;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Ruta;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.TipoEstadoEntrega;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.WhatsappDeContato;
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
  private EntidadBeneficiaria beneficiario;

  @BeforeEach
  void setUp() {
    camion = new Camion("AB123CD", 10f, 2.5f, 1500f);
    chofer = new Chofer("Juan", "Gómez", "12345678");

    WhatsappDeContato contactoWhatsapp =
        new WhatsappDeContato("132212212");

    List<MedioContacto> listaContactos =
        List.of(contactoWhatsapp);

    beneficiario = new EntidadBeneficiariaBuilder()
        .conRazonSocial("Comedor San José")
        .conDireccion("Av. Siempre Viva 123")
        .conMediosContactos(listaContactos)
        .build();

    Donacion donacion = new Donacion(List.of(
        new BienBuilder()
            .conDescripcion("Fideos")
            .conCantidad(5)
            .conUsado(false)
            .buildNoPerecedero()
    ));
    donacion.confirmarAsignacion(beneficiario);

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
    entrega.confirmarListaParaEntregar();

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
    entrega.confirmarListaParaEntregar();
    ruta.iniciarRecorrido();

    assertThrows(IllegalStateException.class, () -> ruta.iniciarRecorrido());
  }
}
