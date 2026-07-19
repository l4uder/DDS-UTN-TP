package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.builder.BeneficiarioBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.CamionBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.*;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion.PlanificadorLogistico;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrquestadorLogisticaTest {
  private Beneficiario beneficiarioA;
  private Beneficiario beneficiarioB;
  private Donacion donacion1;
  private Donacion donacion2;
  private Donacion donacion3;
  private List<Donacion> donaciones;
  private List<Camion> camiones;

  @BeforeEach
  void setUp() {
    MedioContacto contactoWhatsapp = new WhatsappDeContato("132212212");
    MedioContacto contactoSms = new SmsDeContato("112322222");
    MedioContacto contactoCorreo = new CorreoDeContato("comedor@prueba.com");
    MedioContacto contactoCorreoB = new CorreoDeContato("comedorB@prueba.com");

    List<MedioContacto> listaContactosA = List.of(contactoCorreo);
    List<MedioContacto> listaContactosB = List.of(contactoCorreoB);

    beneficiarioA = new BeneficiarioBuilder()
        .conRazonSocial("Comedor A")
        .conDireccion("Av. BeneficiarioA")
        .conAgregarContacto(contactoWhatsapp)
        .build();
    beneficiarioB = new BeneficiarioBuilder()
        .conRazonSocial("Comedor B")
        .conDireccion("Av. BeneficiarioB")
        .conAgregarContacto(contactoSms)
        .build();

    Camion camion = new CamionBuilder().conPatente("AB123CD").conCapacidadVolumen(10f).conAltura(2.5f).conCapacidadCarga(1500f).build();
    camiones = List.of(camion);

    Bien bien = new BienBuilder().conDescripcion("Arroz").conCantidad(3).conUsado(false).buildNoPerecedero();

    Donante donante = new PersonaHumanaBuilder()
      .conNombre("Juan")
      .conApellido("Pérez")
      .conDocumento(new Documento(TipoDocumento.DNI, "12345678"))
      .conContactoPrincipal(new CorreoDeContato("juan@prueba.com"))
      .build();

    donacion1 = new Donacion(List.of(bien), List.of(donante));
    donacion2 = new Donacion(List.of(bien), List.of(donante));
    donacion3 = new Donacion(List.of(bien), List.of(donante));

    donaciones = new ArrayList<>();
  }

  @Test
  void agrupaDonacionesPorDestinoEnEntregas() {
    donacion1.confirmarAsignacion(beneficiarioA);
    donacion2.confirmarAsignacion(beneficiarioA);
    donacion3.confirmarAsignacion(beneficiarioB);
    donaciones = List.of(donacion1, donacion2, donacion3);

    PlanificadorLogistico planificador = new PlanificadorLogistico();
    List<Entrega> entregas = planificador.armarEntregasPendientes(donaciones);
    Entrega entregaBenfeciarioA = entregaPorBeneficiario(beneficiarioA, entregas);
    Entrega entregaBenfeciarioB = entregas.stream().filter(e -> beneficiarioB.esIgual(e.getDestino())).toList().get(0);

    assertEquals(2, entregas.size(), "son dos, porque una es de el beneficiarioA y la otra para el beneficiarioB");
    assertEquals(2, entregaBenfeciarioA.getDonaciones().size(), "el beneficiarioA tiene dos donaciones");
    assertEquals(1, entregaBenfeciarioB.getDonaciones().size(), "el beneficiarioB tiene una donación");
  }

  @Test
  void armaLotesRespetandoElLimiteDeCienDonaciones() {
    donacion1.confirmarAsignacion(beneficiarioA);
    for (int i = 0; i < 150; i++) {
      donaciones.add(donacion1);
    }

    PlanificadorLogistico planificador = new PlanificadorLogistico();
    List<Entrega> entregas = planificador.armarEntregasPendientes(donaciones);
    List<List<Entrega>> lotes = planificador.armarLotesEntrega(entregas);

    assertEquals(1, entregas.size(), "es uno, porque solo hay un beneficiario");
    assertEquals(150, cantDonaciones(entregas), "el único beneficiario tiene ciento cincuenta donaciones");
    assertEquals(2, lotes.size(), "porque 150 es mucho y se debe dividir en dos, uno de cien y el otro de cincuenta");
  }

  @Test
  void armaLotesRespetandoElLimiteDeCienDonacionesParte2() {
    donacion1.confirmarAsignacion(beneficiarioA);
    donacion2.confirmarAsignacion(beneficiarioA);
    donacion3.confirmarAsignacion(beneficiarioB);

    for (int i = 0; i < 60; i++) {
      donaciones.add(donacion1);
    }
    for (int i = 0; i < 30; i++) {
      donaciones.add(donacion2);
    }
    for (int i = 0; i < 20; i++) {
      donaciones.add(donacion3);
    }

    PlanificadorLogistico planificador = new PlanificadorLogistico();
    List<Entrega> entregas = planificador.armarEntregasPendientes(donaciones);
    List<List<Entrega>> lotes = planificador.armarLotesEntrega(entregas);

    assertEquals(2, entregas.size(), "son dos, porque una es de el beneficiarioA y la otra para el beneficiarioB");
    assertEquals(110, cantDonaciones(entregas), "el beneficiarioA tiene 90 donaciones el beneficiarioB tiene 20 donaciones");
    assertEquals(2, lotes.size(), "porque 110 es mucho y se debe dividir en dos, uno de cien y el otro de diez");
  }

  private Entrega entregaPorBeneficiario(Beneficiario beneficiario, List<Entrega> entregas) {
    return entregas.stream().filter(e -> beneficiario.esIgual(e.getDestino()))
        .findFirst().orElseThrow(() -> new DomainValidationException("el beneficiario no existe en esas Entregas"));
  }

  private Integer cantDonaciones(List<Entrega> entregas) {
    return entregas.stream().flatMap(e -> e.getDonaciones().stream()).toList().size();
  }
}
