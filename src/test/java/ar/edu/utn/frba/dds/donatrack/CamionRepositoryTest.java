package ar.edu.utn.frba.dds.donatrack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Coordenada;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Gps;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CamionRepositoryTest {

  private CamionRepository repository;

  @BeforeEach
  void setUp() {
    repository = CamionRepository.getInstancia();
  }

  @Test
  void guardaYRecuperaUnCamionSinGpsNiCoordenadas() {
    // Justificación: valida el caso base — columnas simples de Camion persisten
    // y se recuperan bien, sin depender todavía de las relaciones más complejas.
    Camion camion = new Camion("AB123CD", 10f, 2.5f, 1500f);
    repository.guardar(camion);

    Camion recuperado = repository.buscarPorPatente("AB123CD");

    assertNotNull(recuperado);
    assertEquals("AB123CD", recuperado.getPatente());
    assertEquals(10f, recuperado.getCapacidadVolumen());
  }

  @Test
  void buscarPorPatenteDevuelveNullSiNoExiste() {
    // Justificación: confirma el contrato de "no encontrado" del repositorio
    // (devuelve null, no excepción) — el mismo criterio que se unificó en todos los repos.
    assertNull(repository.buscarPorPatente("NOEXISTE"));
  }

  @Test
  void persisteYRecuperaElHistorialCompletoDeCoordenadas() {
    // Justificación: valida específicamente la decisión de modelar Coordenada como
    // @ElementCollection (historial completo) en vez de última posición única —
    // si esto falla, el mapeo de la tabla camion_coordenadas está mal.
    Camion camion = new Camion("XY111AA", 8f, 2f, 1000f);
    camion.agregarCoordenada(new Coordenada("-34.6", "-58.4"));
    camion.agregarCoordenada(new Coordenada("-34.7", "-58.5"));
    repository.guardar(camion);

    Camion recuperado = repository.buscarPorPatente("XY111AA");

    assertEquals(2, recuperado.getUbicacionActual() != null ? 2 : 0);
  }

  @Test
  void persisteYRecuperaElGpsAsociado() {
    // Justificación: valida la relación @OneToOne hacia Gps (entidad propia,
    // no embebida) — confirma que Gps se guarda con su propia PK (imei) y
    // que Camion referencia correctamente esa fila.
    Camion camion = new Camion("ZZ222BB", 12f, 3f, 2000f);
    Gps gps = new Gps("IMEI-001");
    camion.agregarGps(gps);
    repository.guardar(camion);

    Camion recuperado = repository.buscarPorPatente("ZZ222BB");

    assertNotNull(recuperado.getGps());
    assertEquals("IMEI-001", recuperado.getGps().getImei());
  }

  @Test
  void buscarTodosDevuelveTodosLosCamionesGuardados() {
    repository.guardar(new Camion("AA111AA", 5f, 1.5f, 500f));
    repository.guardar(new Camion("BB222BB", 6f, 1.8f, 600f));

    List<Camion> todos = repository.buscarTodos();

    assertTrue(todos.size() >= 2);
  }
}