package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.*;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.*;
import ar.edu.utn.frba.dds.donatrack.builder.*;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Chofer;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.TipoEstadoEntrega;
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

  private Donante donantePrueba;

  @BeforeEach
  void setUp() {
    camion = new Camion("AB123CD", 10f, 2.5f, 1500f);
    chofer = new Chofer("Juan", "Gómez", "12345678");

    MedioContacto contactoWhatsapp = new WhatsappDeContato("132212212");
    MedioContacto contactoCorreo = new CorreoDeContato("comedor@prueba.com");
    List<MedioContacto> listaContactos = List.of(contactoCorreo);

    donantePrueba = new PersonaHumanaBuilder()
      .conNombre("Juan")
      .conApellido("Pérez")
      .conDocumento(new Documento(TipoDocumento.DNI, "12345678"))
      .conContactoPrincipal(new CorreoDeContato("juan@prueba.com"))
      .build();

    Bien bien = new BienBuilder().conDescripcion("Arroz").conCantidad(3).conUsado(false).buildNoPerecedero();

    beneficiario = new BeneficiarioBuilder().conRazonSocial("Comedor San José").conDireccion("Av. Siempre Viva 123").conAgregarContacto(contactoWhatsapp).build();

    Donante donante = new PersonaHumanaBuilder()
        .conNombre("Juan")
        .conApellido("Pérez")
        .conDocumento(new Documento(TipoDocumento.DNI, "12345678"))
        .conContactoPrincipal(new CorreoDeContato("juan@prueba.com"))
        .build();

    Donacion donacion = new Donacion(List.of(bien), List.of(donante));
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
