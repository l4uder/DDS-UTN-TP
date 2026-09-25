package ar.edu.utn.frba.dds.donatrack.logistica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Gps;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.GpsRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RegistroNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GpsRepositoryTest {
  private GpsRepository repository;

  @BeforeEach
  void setUp() {
    repository = GpsRepository.getInstancia();
  }

  @Test
  void guardaYRecuperaUnGpsPorImei() {
    // Justificación: confirma que Gps persiste como entidad propia con PK natural
    // (imei), independiente de estar o no asociado a un Camion en ese momento —
    // valida la decisión de reclasificarlo de @Embeddable a @Entity.
    Gps gps = new Gps("IMEI-999");
    repository.guardar(gps);

    Gps recuperado = repository.buscarPorId("IMEI-999");

    assertNotNull(recuperado);
    assertEquals("IMEI-999", recuperado.getImei());
    assertEquals("100", recuperado.getNivelBateria());
    assertTrue(recuperado.getFunciona());
  }

  @Test
  void buscarPorIdDevuelveNullSiNoExiste() {
    assertNull(repository.buscarPorId("NOEXISTE"));
  }

  @Test
  void actualizarPersisteCambiosDeEstado() {
    // Justificación: valida que actualizarEstado (nivel de batería, fecha) —
    // el caso de uso real de EstacionRecepcion vía MQTT — se refleje en la base.
    Gps gps = new Gps("IMEI-888");
    repository.guardar(gps);

    gps.actualizarEstado("45");
    repository.actualizar(gps);

    Gps recuperado = repository.buscarPorId("IMEI-888");
    assertEquals("45", recuperado.getNivelBateria());
  }

  @Test
  void actualizarFallaSiElGpsNoExiste() {
    Gps gpsInexistente = new Gps("IMEI-FANTASMA");

    assertThrows(RegistroNoEncontradoException.class, () -> repository.actualizar(gpsInexistente));
  }
}
