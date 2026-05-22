package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.lectores.LectorCsv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LectorTest {
    String archivo;
    String archivoSimple;

    @BeforeEach
    public void configuracionInicial() {
        archivo = "archivosCsv/archivoPrueba.csv"; //20.000 donantes 14.065 validos
        archivoSimple =  "archivosCsv/simple.csv"; //3 donantes 2 validos
    }

    @Test
    public void LectorConUnArchivoSimple(){
        List<Donante> donantes = null;
        donantes = LectorCsv.leerTodo(archivoSimple);

        assertEquals(2, donantes.size());
        //donantes.forEach(d->System.out.println(d));
    }
}
