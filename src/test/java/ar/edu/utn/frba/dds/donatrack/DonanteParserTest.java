package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.dominio.carga_batch.DonanteParser;
import ar.edu.utn.frba.dds.donatrack.dominio.medioContacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.dominio.medioContacto.TelefonoDeContato;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.TipoDocumento;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DonanteParserTest {

    @Test
    public void testParseCsvSuccess() {
        DonanteParser parser = new DonanteParser();
        List<String[]> content = new ArrayList<>();
        content.add(new String[]{"Tipo Doc", "Documento", "Tipo Persona", "Nombre/Razon Social", "Email", "Telefono"});
        content.add(new String[]{"DNI", "12345678", "HUMANA", "Juan Perez", "juan@example.com", "11223344"});

        Iterable<DonanteParser.Resultado> resultados = parser.parseCsv(content);
        Iterator<DonanteParser.Resultado> it = resultados.iterator();

        assertTrue(it.hasNext());
        DonanteParser.Resultado result = it.next();
        assertFalse(result.error());
        assertEquals(1, result.filaNro());
        assertNotNull(result.datosDonante());
        assertEquals("Juan Perez", result.datosDonante().nombreCompleto());
        assertEquals("12345678", result.datosDonante().documento().getDetalle());
        assertEquals(TipoDocumento.DNI, result.datosDonante().documento().getTipoDocumento());
        assertInstanceOf(CorreoDeContato.class, result.datosDonante().contactoPrincipal());
        assertEquals("juan@example.com", ((CorreoDeContato)result.datosDonante().contactoPrincipal()).getCorreo());
        assertInstanceOf(TelefonoDeContato.class, result.datosDonante().contactoSecundario());
        assertEquals("11223344", ((TelefonoDeContato)result.datosDonante().contactoSecundario()).getTelefono());

        assertFalse(it.hasNext());
    }

    @Test
    public void testParseCsvWithError() {
        DonanteParser parser = new DonanteParser();
        // Invalid TipoDocumento will throw IllegalArgumentException in TipoDocumento.valueOf
        List<String[]> content = new ArrayList<>();
        content.add(new String[]{"Tipo Doc", "Documento", "Email"});
        content.add(new String[]{"INVALID", "12345678", "juan@example.com"});
        Iterable<DonanteParser.Resultado> resultados = parser.parseCsv(content);
        Iterator<DonanteParser.Resultado> it = resultados.iterator();

        assertTrue(it.hasNext());
        DonanteParser.Resultado result = it.next();
        assertTrue(result.error());
        assertEquals(1, result.filaNro());
        assertNull(result.datosDonante());
    }

    @Test
    public void testParseCsvOnDemand() {
        DonanteParser parser = new DonanteParser();
        // Counter to track how many times content is accessed
        final int[] accessCount = {0};
        Iterable<String[]> content = () -> new Iterator<>() {
            private int count = 0;
            private boolean headerSent = false;
            @Override
            public boolean hasNext() {
                return count < 3;
            }

            @Override
            public String[] next() {
                if (!headerSent){
                    headerSent = true;
                    return new String[]{"Nombre/Razon Social"};
                }
                count++;
                accessCount[0]++;
                return new String[]{"Persona " + count};
            }
        };

        Iterable<DonanteParser.Resultado> resultados = parser.parseCsv(content);
        assertEquals(0, accessCount[0], "Should not have accessed content yet");

        Iterator<DonanteParser.Resultado> it = resultados.iterator();
        assertEquals(0, accessCount[0], "Should not have accessed content yet even after getting iterator");

        assertTrue(it.hasNext());
        it.next();
        assertEquals(1, accessCount[0], "Should have accessed first row");

        assertTrue(it.hasNext());
        it.next();
        assertEquals(2, accessCount[0], "Should have accessed second row");
    }
}
