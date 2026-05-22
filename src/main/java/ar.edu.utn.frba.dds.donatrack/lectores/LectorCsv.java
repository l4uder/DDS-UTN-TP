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

    public static List<Donante> leerTodo(String csvDonantes) {
        List<Donante> donantes = new ArrayList<>();

        InputStream csvStream = LectorCsv.class.getClassLoader().getResourceAsStream(csvDonantes);

        if (csvStream == null) {
            System.out.println("No se encontró el archivo CSV en resources: " + csvDonantes);
            return donantes;
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8))) {

            reader.readNext(); // lectura del encabezado

            String[] fila;
            while ((fila = reader.readNext()) != null) {
                String tipoPersona = fila[0];
                Documento documento = new Documento(TipoDocumento.valueOf(fila[1]) ,fila[2]);
                String nombreCompleto = fila[3];
                MedioContacto contactoPrincipal = new MedioContacto(TipoContacto.CORREO, fila[4]) ;
                MedioContacto contactoSecundario = new MedioContacto(TipoContacto.TELEFONO, fila[5]);

                Donante donanteNuevo = DonanteSimpleFactory.crear(tipoPersona, documento, nombreCompleto, contactoPrincipal, contactoSecundario);

                Donante donanteEncontrado = donantes.stream().filter(d -> d.esElMismo(donanteNuevo)).findFirst().orElse(null);
                if(donanteEncontrado == null)
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

        return donantes;
    }
}





