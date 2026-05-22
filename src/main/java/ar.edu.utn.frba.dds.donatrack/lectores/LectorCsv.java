package ar.edu.utn.frba.dds.donatrack.lectores;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LectorCsv {

    public static List<Donante> leerTodo(String csvDonantes) {
        List<Donante> donantes = new ArrayList<>();
        CSVReader reader = null;

        InputStream csvStream = LectorCsv.class.getClassLoader().getResourceAsStream(csvDonantes);

        if (csvStream == null) {
            System.out.println("No se encontró el archivo CSV en resources: " + csvDonantes);
            return donantes;
        }

        try {
            reader = new CSVReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8));
            reader.readNext(); // lecura del encabezado

            String[] fila;
            while ((fila = reader.readNext()) != null) {
                String tipoPersona = fila[0];
                String documento = fila[1] + fila[2];
                String nombreCompleto = fila[3];
                String mail = fila[4];
                String telefono = fila[5];

                //donantes.add(donante);
                System.out.println("tipoPersona: " + tipoPersona);
                System.out.println("documento: " + documento);
                System.out.println("nombreCompleto: " + nombreCompleto);
                System.out.println("mail: " + mail);
                System.out.println("telefono: " + telefono);
            }
        } catch (CsvValidationException c) {
            System.out.println("El archivo CSV tiene formato inválido");
            c.printStackTrace();
        } catch (IOException io) {
            System.out.println("Error al leer el archivo CSV");
            io.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException io) {
                    System.out.println("Error al cerrar el archivo CSV");
                    io.printStackTrace();
                }
            }
        }

        return donantes;
    }
}





