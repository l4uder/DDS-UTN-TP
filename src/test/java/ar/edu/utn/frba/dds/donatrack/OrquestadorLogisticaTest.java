package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.orquestadorlogistica.OrquestadorLogistica;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.BeneficiarioDTO;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.DonacionAsignadaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrquestadorLogisticaTest {

  private BeneficiarioDTO beneficiarioA;
  private BeneficiarioDTO beneficiarioB;
  private List<DonacionAsignadaDTO> donaciones;
  private List<Camion> camiones;

  @BeforeEach
  void setUp() {
    beneficiarioA = new BeneficiarioDTO("ben-a", "Comedor A", "Av. BeneficiarioA");
    beneficiarioB = new BeneficiarioDTO("ben-b", "Comedor B", "Av. BeneficiarioB");

    donaciones = new ArrayList<>();
    camiones = List.of(new Camion("AB123CD", 10f, 2.5f, 1500f));
  }

  private DonacionAsignadaDTO donacionAsignadaA(BeneficiarioDTO beneficiario, int nro) {
    return new DonacionAsignadaDTO(
        "don-" + beneficiario.getId() + "-" + nro,
        "Arroz",
        beneficiario
    );
  }

  @Test
  void agrupaDonacionesPorDestinoEnEntregas() {
    donaciones.add(donacionAsignadaA(beneficiarioA, 1));
    donaciones.add(donacionAsignadaA(beneficiarioA, 2));
    donaciones.add(donacionAsignadaA(beneficiarioB, 1));

    OrquestadorLogistica orquestador = new OrquestadorLogistica(camiones, donaciones);
    List<Entrega> entregas = orquestador.armarEntregasPendientes();

    assertEquals(2, entregas.size());
    assertTrue(entregas.stream().anyMatch(e ->
        e.getDestino().equals(beneficiarioA) && e.getDonaciones().size() == 2));
    assertTrue(entregas.stream().anyMatch(e ->
        e.getDestino().equals(beneficiarioB) && e.getDonaciones().size() == 1));
  }

  @Test
  void armaLotesRespetandoElLimiteDeCienDonaciones() {
    for (int i = 0; i < 150; i++) {
      donaciones.add(donacionAsignadaA(beneficiarioA, i));
    }

    OrquestadorLogistica orquestador = new OrquestadorLogistica(camiones, donaciones);
    List<Entrega> entregas = orquestador.armarEntregasPendientes();
    List<List<Entrega>> lotes = orquestador.armarLotesEntrega(entregas);

    assertFalse(lotes.isEmpty());
  }

}
