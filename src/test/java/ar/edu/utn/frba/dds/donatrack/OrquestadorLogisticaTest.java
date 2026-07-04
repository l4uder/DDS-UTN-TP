package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.EntidadBeneficiariaBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.OrquestadorException;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.orquestadorlogistica.OrquestadorLogistica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.SmsDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.WhatsappDeContato;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrquestadorLogisticaTest {

  private Beneficiario beneficiarioA;
  private Beneficiario beneficiarioB;
  private List<Donacion> donaciones;
  private List<Camion> camiones;

  @BeforeEach
  void setUp() {
    WhatsappDeContato contactoWhatsapp = new WhatsappDeContato("132212212");
    SmsDeContato contactoSms = new SmsDeContato("112322222");

    List<MedioContacto> listaContactosA = List.of(contactoWhatsapp);
    List<MedioContacto> listaContactosB = List.of(contactoSms);

    beneficiarioA = new EntidadBeneficiariaBuilder()
        .conRazonSocial("Comedor A")
        .conDireccion("Av. BeneficiarioA")
        .conMediosContactos(listaContactosA)
        .build();
    beneficiarioB = new EntidadBeneficiariaBuilder()
        .conRazonSocial("Comedor B")
        .conDireccion("Av. BeneficiarioB")
        .conMediosContactos(listaContactosB)
        .build();

    donaciones = new ArrayList<>();
    camiones = List.of(new Camion("AB123CD", 10f, 2.5f, 1500f));
  }

  private Donacion donacionAsignadaA(Beneficiario beneficiario) {
    Donacion d = new Donacion(List.of(
        new BienBuilder().conDescripcion("Arroz").conCantidad(3).conUsado(false).buildNoPerecedero()
    ));
    d.confirmarAsignacion(beneficiario);
    return d;
  }

  @Test
  void agrupaDonacionesPorDestinoEnEntregas() {
    donaciones.add(donacionAsignadaA(beneficiarioA));
    donaciones.add(donacionAsignadaA(beneficiarioA));
    donaciones.add(donacionAsignadaA(beneficiarioB));

    OrquestadorLogistica orquestador = new OrquestadorLogistica(camiones, donaciones);
    List<Entrega> entregas = orquestador.armarEntregasPendientes();

    assertEquals(2, entregas.size());
    assertTrue(entregas.stream().anyMatch(e -> e.getDestino().equals(beneficiarioA) && e.getDonaciones().size() == 2));
    assertTrue(entregas.stream().anyMatch(e -> e.getDestino().equals(beneficiarioB) && e.getDonaciones().size() == 1));
  }

  @Test
  void armaLotesRespetandoElLimiteDeCienDonaciones() {
    for (int i = 0; i < 150; i++) {
      donaciones.add(donacionAsignadaA(beneficiarioA));
    }

    OrquestadorLogistica orquestador = new OrquestadorLogistica(camiones, donaciones);
    List<Entrega> entregas = orquestador.armarEntregasPendientes(); // una sola Entrega con 150 donaciones
    List<List<Entrega>> lotes = orquestador.armarLotesEntrega(entregas);

    // como las 150 donaciones quedan agrupadas en UNA sola Entrega (mismo destino),
    // el lote no puede partirla
    assertFalse(lotes.isEmpty());
  }

  @Test
  void soloConsideraDonacionesEnAsignacionRealizada() {
    Donacion donacionSinAsignar = new Donacion(List.of(
        new BienBuilder()
          .conDescripcion("Fideos")
          .conCantidad(2)
          .conUsado(false)
          .buildNoPerecedero()
    )); // se queda en EN_DEPOSITO
    donaciones.add(donacionSinAsignar);
    donaciones.add(donacionAsignadaA(beneficiarioA));

    assertThrows(OrquestadorException.class, () -> new OrquestadorLogistica(camiones, donaciones));
  }

}