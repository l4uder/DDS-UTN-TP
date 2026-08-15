package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.*;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
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

  private Donante donantePrueba;

  @BeforeEach
  void setUp() {
    camion = new Camion("AB123CD", 10f, 2.5f, 1500f);
    chofer = new Chofer("Juan", "Gómez", "12345678");

    MedioContacto contactoWhatsapp = new WhatsappDeContato("132212212", true);
    MedioContacto contactoCorreo = new CorreoDeContato("comedor@prueba.com", true);
    List<MedioContacto> listaContactos = List.of(contactoCorreo);

    donantePrueba = new PersonaHumanaBuilder()
        .conNombre("Juan")
        .conApellido("Pérez")
        .conDocumento(new Documento(TipoDocumento.DNI, "12345678"))
        .conAgregarContacto(new CorreoDeContato("juan@prueba.com", true))
        .conDireccion("alguna dirección")
        .build();

    //donacion.confirmarAsignacion(beneficiario);
    beneficiario = new Beneficiario("ben-1", "Comedor San José", "Av. Siempre Viva 123");

    DonacionEnTransito donacion = new DonacionEnTransito("don-1", "Fideos", beneficiario);

    entrega = new Entrega(beneficiario, List.of(donacion), camion);
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
