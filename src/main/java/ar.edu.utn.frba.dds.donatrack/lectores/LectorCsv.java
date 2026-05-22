package ar.edu.utn.frba.dds.donatrack.lectores;

import ar.edu.utn.frba.dds.donatrack.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donante.DonanteSimpleFactory;
import ar.edu.utn.frba.dds.donatrack.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LectorCsv {

    public record Error(int fila, String nombre){}

    public record ResultadoImportacion(List<Error> errores, List<Donante> donantes){}

    public static ResultadoImportacion leerTodo(String csvDonantes) {
        List<Donante> donantes = new ArrayList<>();

        InputStream csvStream = LectorCsv.class.getClassLoader().getResourceAsStream(csvDonantes);

        if (csvStream == null) {
            String msg = "No se encontró el archivo CSV en resources: " + csvDonantes;
            System.out.println(msg);
            throw new BatchJobException(msg);
        }

        var registroErrores = new ArrayList<Error>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8))) {

            reader.readNext(); // lectura del encabezado

            String[] fila;
            int indice = 0;
            while ((fila = reader.readNext()) != null) {
                indice++;
                String tipoPersona = fila[0];
                Documento documento = new Documento(TipoDocumento.valueOf(fila[1]), fila[2]);
                String nombreCompleto = fila[3];
                MedioContacto contactoPrincipal = new MedioContacto(TipoContacto.CORREO, fila[4]);
                MedioContacto contactoSecundario = null;

                if (!fila[5].isBlank()){
                     contactoSecundario = new MedioContacto(TipoContacto.TELEFONO, fila[5]);
                }

                Donante donanteNuevo;
                try {
                    donanteNuevo = DonanteSimpleFactory.crear(tipoPersona, documento, nombreCompleto, contactoPrincipal, contactoSecundario);
                } catch (IllegalArgumentException e) {
                    registroErrores.add(new Error(indice, nombreCompleto));
                    continue;
                }

                Donante donanteEncontrado = donantes.stream().filter(d -> d.esElMismo(donanteNuevo)).findFirst().orElse(null);
                if (donanteEncontrado == null)
                    donantes.add(donanteNuevo);
                else {
                    donanteEncontrado.actualizar(donanteNuevo);
                }
            }
        } catch (CsvValidationException e) {
            String msg = "El archivo CSV tiene formato inválido";
            System.out.println(msg);
            throw new BatchJobException(msg, e);
        } catch (IOException e) {
            String msg = "Error al leer el archivo CSV";
            System.out.println(msg);
            throw new BatchJobException(msg, e);
        }

        return new ResultadoImportacion(registroErrores, donantes);
    }
}





