package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes.importadorcsv;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.DonanteFactory;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoPersona;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes.FilaError;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.SmsDeContato;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.ImportadorCsvException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import com.opencsv.CSVParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportadorCsv {

  public record FilaLeida(String contenido, Integer numFila) { }
  public record FilaParseada(String[] contenidoSeparado, Integer numFila, String motivoError) { }
  public record FilaTransformada(Donante donante, Integer numFila, String motivoError) { }

  public ResultadoImportacion importarDesdeArchivo(String pathArchivo) {
    InputStream csvStream = abrirArchivo(pathArchivo);
    List<FilaLeida> filasLeidas = leerArchivo(csvStream); //Nota si leemos archivos demasiado grandes, va a ser mejor que leer y parsear sean un solo metodo y usar Iterator
    List<FilaParseada> filasParseadas = parsearValidar(filasLeidas);
    List<FilaTransformada> filasTransformadas = transformarValidar(filasParseadas);

    return new ResultadoImportacion(
        filasTransformadas.stream().filter(f -> f.donante() != null).map(f -> f.donante()).toList(),
        filasTransformadas.stream().filter(f -> f.motivoError() != null).map(f -> new FilaError(f.numFila(), f.motivoError())).toList()
    );
  }

  //================ FUNCIONES AUXILIARES ==================
  private InputStream abrirArchivo(String pathArchivo) {
    InputStream csvStream = ImportadorCsv.class.getClassLoader().getResourceAsStream(pathArchivo);
    if (csvStream == null)
      throw new ImportadorCsvException("No se encontró el archivo CSV en resources: " + pathArchivo);

    return csvStream;
  }

  private List<FilaLeida> leerArchivo(InputStream csvStream) {
    List<FilaLeida> filasLeidas = new ArrayList<>();
    FilaLeida fila = null;
    BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8));

    try {
      String linea = null;
      int numFila = 0;
      while ((linea = reader.readLine()) != null) {
        fila = new FilaLeida(linea, numFila);
        if (numFila != 0) // Nos saltamos el encabezado
          filasLeidas.add(fila);
        numFila++;
      }
    } catch (IOException exIO) {
      throw new ImportadorCsvException("Error físico al leer el archivo", exIO);
    } finally {
      try {
        reader.close();
      } catch (IOException exIO) {
        System.err.println("Advertencia: No se pudo cerrar el archivo - " + exIO.getMessage());      }
    }

    return filasLeidas;
  }

  private List<FilaParseada> parsearValidar(List<FilaLeida> filasLeidas) {
    List<FilaParseada> filasParseadas = new ArrayList<>();
    FilaParseada fila = null;
    CSVParser parser = new CSVParser();

    for (FilaLeida filaLeida : filasLeidas) {
      try {
        String[] contenidoSeparado = parser.parseLine(filaLeida.contenido());
        if (contenidoSeparado.length == 6) {
          if (yaExisteEstaFilaParseada(contenidoSeparado[4], filasParseadas)) {
            FilaParseada filaObsoleta = filasParseadas.stream().filter(fp -> fp.contenidoSeparado() != null && fp.contenidoSeparado()[4].equalsIgnoreCase(contenidoSeparado[4])).findFirst().orElseThrow(() -> new RuntimeException("No se encontró el obsoleto, cuando se dijo que existia"));
            filasParseadas.add(new FilaParseada(null, filaObsoleta.numFila(), "existe una version mas nueva en la fila " + filaLeida.numFila()));
            filasParseadas.remove(filaObsoleta);
          }
          fila = new FilaParseada(contenidoSeparado, filaLeida.numFila(), null);
        } else {
          fila = new FilaParseada(null, filaLeida.numFila(), "La fila debe tener 6 columnas");
        }
      } catch (IOException exIO) {
        fila = new FilaParseada(null, filaLeida.numFila(), "Error al parsear la linea: " + exIO.getMessage());
      }
      filasParseadas.add(fila);
    }

    return filasParseadas;
  }

  private List<FilaTransformada> transformarValidar(List<FilaParseada> filasParseadas) {
    List<FilaTransformada> filasTransformadas = new ArrayList<>();
    FilaTransformada fila = null;

    for (FilaParseada filaParseada : filasParseadas) {
      if (filaParseada.motivoError() == null) { //if (No tiene error)
        try {
          String[] contenido = filaParseada.contenidoSeparado();
          TipoPersona persona = aTipoPersona(contenido[0].trim());
          Documento documento = new Documento(aTipoDocumento(contenido[1].trim(), persona), contenido[2].trim());
          String nombreCompleto = contenido[3].trim();
          MedioContacto correo = new CorreoDeContato(contenido[4].trim(), true);
          MedioContacto telefono = new SmsDeContato(contenido[5].trim(), false);
          Donante donante = DonanteFactory.crear(persona, documento, nombreCompleto, correo, telefono);
          fila = new FilaTransformada(donante, filaParseada.numFila(), null);
        } catch (DominioException exV){
          fila = new FilaTransformada(null, filaParseada.numFila(), exV.getMessage());
        }
      } else {
        fila = new FilaTransformada(null, filaParseada.numFila(), filaParseada.motivoError());
      }
      filasTransformadas.add(fila);
    }

    return filasTransformadas;
  }

  //======================== FUNCIONES AUXILIARES =============================
  private boolean yaExisteEstaFilaParseada(String correo, List<FilaParseada> filasParseadas) {
    if (correo == null || correo.isBlank()) return false;
    return filasParseadas.stream().filter(fp -> fp.contenidoSeparado() != null)
        .anyMatch(fp -> fp.contenidoSeparado()[4].equalsIgnoreCase(correo));
  } //Nota se podría mejorar la velocidad si lo cambiamos a hashmap y ya no usaríamos .any

  private static TipoPersona aTipoPersona(String valor) {
    if (valor == null || valor.isBlank()) {
      throw new DominioException("El csv No especifica el tipo de persona, valores validos: " + Arrays.toString(TipoPersona.values()));    }
    try {
      return TipoPersona.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DominioException("El tipo de persona: " + valor + " del csv no existe, valores validos: " + Arrays.toString(TipoPersona.values()));
    }
  }

  private static TipoDocumento aTipoDocumento(String tipoDocumento, TipoPersona tipoPersona) {
    if (tipoDocumento == null || tipoDocumento.isBlank()) {
      throw new DominioException("El tipo de documento del csv, valores validos: " + TipoDocumento.values(tipoPersona));
    }
    try {
      return TipoDocumento.valueOf(tipoDocumento.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DominioException("El tipo de documento: " + tipoDocumento + " del csv no existe, valores validos: " + TipoDocumento.values(tipoPersona));
    }
  }

}
