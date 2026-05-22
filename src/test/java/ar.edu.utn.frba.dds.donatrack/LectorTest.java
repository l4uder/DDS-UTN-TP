package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.lectores.LectorCsv;
import org.junit.jupiter.api.Test;

public class LectorTest {
    @Test
    public void LectorConUnArchivoSimple(){
        String path = "archivosCsv/simple.csv";

        LectorCsv.leerTodo(path);
    }
}
