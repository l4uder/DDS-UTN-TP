package ar.edu.utn.frba.dds.donatrack.logistica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta.Chofer;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.RutaRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BaseDatoException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RutaRepositoryTest {
  private RutaRepository rutaRepository;
  private EntregaRepository entregaRepository;
  private BeneficiarioRepository beneficiarioRepository;
  private CamionRepository camionRepository;

  @BeforeEach
  void setUp() {
    rutaRepository = RutaRepository.getInstancia();
    entregaRepository = EntregaRepository.getInstancia();
    beneficiarioRepository = BeneficiarioRepository.getInstancia();
    camionRepository = CamionRepository.getInstancia();
  }

  @Test
  void guardaYRecuperaUnaRutaConEntregasEnElOrdenCorrecto() {
    // Justificación: valida @OneToMany + @OrderColumn en Ruta.entregasOrdenadas —
    // a diferencia de Entrega (que usaba @ElementCollection), acá la colección
    // es de otra @Entity, y la FK vive del lado de Entrega (ruta_id), no en Ruta.
    Camion camion = new Camion("DD444DD", 10f, 2.5f, 1500f);
    camionRepository.guardar(camion);

    Beneficiario beneficiario = new Beneficiario(5L, "Comedor E", "Calle 5");
    beneficiarioRepository.guardar(beneficiario);

    DonacionEnTransito donacion1 = new DonacionEnTransito(5L, "Fideos", beneficiario);
    Entrega entrega1 = new Entrega(List.of(donacion1));
    entregaRepository.guardar(entrega1);

    DonacionEnTransito donacion2 = new DonacionEnTransito(6L, "Arroz", beneficiario);
    Entrega entrega2 = new Entrega(List.of(donacion2));
    entregaRepository.guardar(entrega2);

    Ruta ruta = new Ruta(camion, LocalDate.now(), List.of(entrega1, entrega2));
    rutaRepository.guardar(ruta);

    assertNotNull(ruta.getId());
    Ruta recuperada = rutaRepository.buscarPorId(ruta.getId());

    assertEquals(2, recuperada.getEntregasOrdenadas().size());
    assertEquals(entrega1.getId(), recuperada.getEntregasOrdenadas().get(0).getId());
    assertEquals(entrega2.getId(), recuperada.getEntregasOrdenadas().get(1).getId());
  }

  @Test
  void guardarFallaSiLaRutaYaTieneIdAsignado() {
    // Justificación: valida el guard de "no persistir dos veces la misma instancia"
    // que se mantuvo del diseño original, ahora protegiendo contra el uso incorrecto
    // de persist() sobre una entidad que ya tiene id (comportamiento indefinido en JPA).
    Camion camion = new Camion("EE555EE", 10f, 2.5f, 1500f);
    camionRepository.guardar(camion);
    Ruta ruta = new Ruta(camion, LocalDate.now(), List.of());
    rutaRepository.guardar(ruta);

    assertThrows(BaseDatoException.class, () -> rutaRepository.guardar(ruta));
  }

  @Test
  void actualizarPersisteLaAsignacionDeChofer() {
    Camion camion = new Camion("FF666FF", 10f, 2.5f, 1500f);
    camionRepository.guardar(camion);
    Ruta ruta = new Ruta(camion, LocalDate.now(), List.of());
    rutaRepository.guardar(ruta);

    Chofer chofer = new Chofer("Juan", "Pérez", "12345678");
    ruta.asignarChofer(chofer);
    rutaRepository.actualizar(ruta);

    Ruta recuperada = rutaRepository.buscarPorId(ruta.getId());
    assertNotNull(recuperada.getChofer());
    assertEquals("12345678", recuperada.getChofer().getLicenciaConducir());
  }
}
