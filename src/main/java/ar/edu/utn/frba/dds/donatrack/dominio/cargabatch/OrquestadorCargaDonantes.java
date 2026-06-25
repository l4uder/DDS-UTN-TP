package ar.edu.utn.frba.dds.donatrack.dominio.cargabatch;

import ar.edu.utn.frba.dds.donatrack.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.DonanteFactory;
import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.BatchJobException;
import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.persistencia.DonanteRepository;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OrquestadorCargaDonantes {

  public record Error(int fila) {
  }

  public record ResultadoImportacion(List<Error> errores, int registrosProcesados) {
  }

  public static ResultadoImportacion iniciarCarga(String pathArchivoCsv) {
    int cargadosCorrectamente = 0;

    InputStream csvStream = OrquestadorCargaDonantes.class
        .getClassLoader()
        .getResourceAsStream(pathArchivoCsv);

    if (csvStream == null) {
      String msg = "No se encontró el archivo CSV en resources: " + pathArchivoCsv;
      throw new BatchJobException(msg);
    }

    var registroErrores = new ArrayList<Error>();
    try (CSVReader reader = new CSVReader(
        new InputStreamReader(csvStream, StandardCharsets.UTF_8))) {
      DonanteParser parser = new DonanteParser();
      DonanteRepository repository = DonanteRepository.getInstancia();
      var iter = parser.parseCsv(reader).iterator();

      while (iter.hasNext()) {
        var resultado = iter.next();

        if (resultado.error()) {
          registroErrores.add(new Error(resultado.filaNro()));
          continue;
        }
        Donante donanteNuevo;
        try {
          donanteNuevo = DonanteFactory.crear(
              resultado.datosDonante().tipoPersona(),
              resultado.datosDonante().documento(),
              resultado.datosDonante().nombreCompleto(),
              resultado.datosDonante().contactoPrincipal(),
              resultado.datosDonante().contactoSecundario()
          );
        } catch (DomainValidationException e) {
          registroErrores.add(new Error(resultado.filaNro()));
          continue;
        }
        repository.guardarDonante(donanteNuevo);
        cargadosCorrectamente++;
      }
    } catch (IOException e) {
      String msg = "Error al leer el archivo CSV";
      System.out.println(msg);
      throw new BatchJobException(msg, e);
    }

    return new ResultadoImportacion(registroErrores, cargadosCorrectamente);
  }
}