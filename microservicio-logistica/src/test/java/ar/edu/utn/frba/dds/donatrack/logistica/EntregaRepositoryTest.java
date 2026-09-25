package ar.edu.utn.frba.dds.donatrack.logistica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.TipoEstadoEntrega;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntregaRepositoryTest {
  private EntregaRepository entregaRepository;
  private BeneficiarioRepository beneficiarioRepository;
  private CamionRepository camionRepository;

  @BeforeEach
  void setUp() {
    entregaRepository = EntregaRepository.getInstancia();
    beneficiarioRepository = BeneficiarioRepository.getInstancia();
    camionRepository = CamionRepository.getInstancia();
  }

  @Test
  void guardaYRecuperaUnaEntregaConDonacionesYBeneficiario() {
    // Justificación: es el test más importante de todo el modelo — valida el
    // caso más anidado: @ElementCollection (donaciones) que contiene un
    // @Embeddable (DonacionEnTransito) con una relación @ManyToOne hacia
    // Beneficiario (entidad). Si algo del mapeo de herencia de colecciones
    // está mal, este test lo revela.
    Beneficiario beneficiario = new Beneficiario(10L, "Comedor San José", "Av. Siempre Viva 123");
    beneficiarioRepository.guardar(beneficiario);

    DonacionEnTransito donacion = new DonacionEnTransito(10L, "Fideos", beneficiario);
    Entrega entrega = new Entrega(List.of(donacion));

    entregaRepository.guardar(entrega);

    assertNotNull(entrega.getId()); // confirma que @GeneratedValue funcionó
    Entrega recuperada = entregaRepository.buscarPorId(entrega.getId());

    assertNotNull(recuperada);
    assertEquals(1, recuperada.getDonaciones().size());
    assertEquals(10L, recuperada.getDonaciones().get(0).getBeneficiario().getId()
    );
  }

  @Test
  void unaEntregaNuevaPersisteEnEstadoPendiente() {
    Beneficiario beneficiario = new Beneficiario(20L, "Comedor B", "Calle 2");
    beneficiarioRepository.guardar(beneficiario);
    DonacionEnTransito donacion = new DonacionEnTransito(20L, "Arroz", beneficiario);
    Entrega entrega = new Entrega(List.of(donacion));

    entregaRepository.guardar(entrega);
    Entrega recuperada = entregaRepository.buscarPorId(entrega.getId());

    assertEquals(TipoEstadoEntrega.PENDIENTE, recuperada.getEstadoActual());
  }

  @Test
  void elHistorialDeEstadosPreservaElOrdenAlRecuperar() {
    Beneficiario beneficiario = new Beneficiario(30L, "Comedor C", "Calle 3");
    beneficiarioRepository.guardar(beneficiario);

    DonacionEnTransito donacion = new DonacionEnTransito(30L, "Fideos", beneficiario);

    Camion camion = new Camion("CC333CC", 5f, 2f, 500f);
    camionRepository.guardar(camion);

    Entrega entrega = new Entrega(List.of(donacion));
    entregaRepository.guardar(entrega);

    entrega.reasignarCamion(camion);
    entrega.confirmarListaParaEntregar();

    entregaRepository.actualizar(entrega);

    Entrega recuperada = entregaRepository.buscarPorId(entrega.getId());

    assertEquals(3, recuperada.getHistorialEstados().size());
    assertEquals( TipoEstadoEntrega.PENDIENTE, recuperada.getHistorialEstados().get(0).getTipoEstado());
    assertEquals(TipoEstadoEntrega.PENDIENTE, recuperada.getHistorialEstados().get(1).getTipoEstado());
    assertEquals(TipoEstadoEntrega.LISTA_PARA_ENTREGAR, recuperada.getHistorialEstados().get(2).getTipoEstado());
    assertEquals(TipoEstadoEntrega.LISTA_PARA_ENTREGAR, recuperada.getEstadoActual());
    assertNull(recuperada.getHistorialEstados().get(0).getCamion());
    assertEquals("CC333CC", recuperada.getHistorialEstados().get(1).getCamion().getPatente());
    assertEquals("CC333CC", recuperada.getHistorialEstados().get(2).getCamion().getPatente());
  }

  @Test
  void eliminarBorraLaEntregaYSusColeccionesAsociadas() {
    Beneficiario beneficiario = new Beneficiario(4L, "Comedor D", "Calle 4");
    beneficiarioRepository.guardar(beneficiario);
    DonacionEnTransito donacion = new DonacionEnTransito(4L, "Fideos", beneficiario);
    Entrega entrega = new Entrega(List.of(donacion));
    entregaRepository.guardar(entrega);
    Long id = entrega.getId();

    entregaRepository.eliminar(id);

    assertNull(entregaRepository.buscarPorId(id));
  }

  @Test
  void eliminarFallaSiLaEntregaNoExiste() {
    assertThrows(RegistroNoEncontradoException.class, () -> entregaRepository.eliminar(137L));
  }

}
