package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ProveedorClienteCorreo;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.EntidadBeneficiariaBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Chofer;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.TipoEstadoEntrega;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.WhatsappDeContato;
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

    WhatsappDeContato contactoWhatsapp =
        new WhatsappDeContato("132212212");

    CorreoDeContato contactoCorreo = 
      new CorreoDeContato("comedor@prueba.com");
    
    List<MedioContacto> listaContactos =
        List.of(contactoCorreo); //Cambio a correo a la espera de la implementación de envio por Whatsapp

    beneficiario = new EntidadBeneficiariaBuilder()
        .conRazonSocial("Comedor San José")
        .conDireccion("Av. Siempre Viva 123")
        .conMediosContactos(listaContactos)
        .build();

    //Mock de Motor de correos exclusivo para los tests
    ProveedorClienteCorreo.inicializar((destino, mensaje) -> {
        // No hace nada de red, solo simula que lo envió
        System.out.println("TEST - Simulando envío a: " + destino);
    });

    donantePrueba = new PersonaHumanaBuilder()
      .conNombre("Juan")
      .conApellido("Pérez")
      .conDocumento(new Documento(TipoDocumento.DNI, "12345678"))
      .conContactoPrincipal(new CorreoDeContato("juan@prueba.com"))
      .build();

    Donacion donacion = new Donacion(donantePrueba, List.of(
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
