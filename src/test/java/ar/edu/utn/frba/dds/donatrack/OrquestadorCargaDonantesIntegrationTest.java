package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.dominio.cargabatch.OrquestadorCargaDonantes;
import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.BatchJobException;
import ar.edu.utn.frba.dds.donatrack.persistencia.DonanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrquestadorCargaDonantesIntegrationTest {
  private String archivoSimple;

  @BeforeEach
  public void configuracionInicial() {
      archivoSimple = "simple.csv"; //3 donantes 2 validos
  }

  @Test
  public void LectorConUnArchivoSimple(){
    var donantesIniciales = DonanteRepository.INSTANCE.buscarTodos();
    assertTrue(donantesIniciales.isEmpty());
    var resultado = OrquestadorCargaDonantes.iniciarCarga(archivoSimple);
    var donantesResultantes = DonanteRepository.INSTANCE.buscarTodos();

    //System.out.println("Los errores encontrados son: " + resultado.errores());

    assertEquals(1, resultado.errores().size());
    assertEquals(3, resultado.registrosProcesados());
    assertEquals(2, donantesResultantes.size());
  }

  @Test
  public void LectorLanzaExcepcionSiElArchivoNoExiste() {
    String archivoInexistente = "no_existo.csv";

    // Si "iniciarCarga" NO lanza la BatchJobException, el test va a fallar automáticamente aquí.
    assertThrows(BatchJobException.class, () -> {
      OrquestadorCargaDonantes.iniciarCarga(archivoInexistente);
    });
  }
}
