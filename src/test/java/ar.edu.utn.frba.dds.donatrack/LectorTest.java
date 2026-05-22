package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.lectores.LectorCsv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LectorTest {
    String archivoSimple;

    @BeforeEach
    public void configuracionInicial() {
//        archivo = "simple.csv"; //20.000 donantes 14.065 validos
        archivoSimple = "simple.csv"; //3 donantes 2 validos
    }

    @Test
    public void LectorConUnArchivoSimple(){
        var resultado = LectorCsv.leerTodo(archivoSimple);

        assertEquals(2, resultado.donantes().size());
    }
}
