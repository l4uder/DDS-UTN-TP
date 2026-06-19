package ar.edu.utn.frba.dds.donatrack.dominio.cargabatch;

import ar.edu.utn.frba.dds.donatrack.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.TelefonoDeContato;
import java.text.Normalizer;
import java.util.Iterator;
import java.util.regex.Pattern;

public class DonanteParser {

  public record DatosDonante(Documento documento, String tipoPersona, String nombreCompleto,
                             MedioContacto contactoPrincipal, MedioContacto contactoSecundario) {
  }

  public record Resultado(DatosDonante datosDonante, boolean error, int filaNro) {}

  public Iterable<Resultado> parseCsv(Iterable<String[]> csvContent) {
    Iterator<String[]> iterator = csvContent.iterator();
    if (!iterator.hasNext()) {
      throw new DomainValidationException("Archivo csv sin header");
    }
    String[] csvHeader = iterator.next();
    int tipoPersonaPos = -1;
    int tipoDocPos = -1;
    int docPos = -1;
    int nombrePos = -1;
    int emailPos = -1;
    int telPos = -1;

    // Use regex to remove all "Combining Diacritical Marks"
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    for (var i = 0; i < csvHeader.length; i++) {
      var colName = Normalizer.normalize(csvHeader[i], Normalizer.Form.NFD);
      colName = pattern.matcher(colName).replaceAll("").toLowerCase().replace(" ", "");
      switch (colName) {
        case "tipopersona" -> tipoPersonaPos = i;
        case "tipodoc" -> tipoDocPos = i;
        case "documento" -> docPos = i;
        case "nombre/razonsocial" -> nombrePos = i;
        case "email" -> emailPos = i;
        case "telefono" -> telPos = i;
        default -> { }
      }
    }

    final int finalTipoPersonaPos = tipoPersonaPos;
    final int finalTipoDocPos = tipoDocPos;
    final int finalDocPos = docPos;
    final int finalNombrePos = nombrePos;
    final int finalEmailPos = emailPos;
    final int finalTelPos = telPos;

    return () -> new Iterator<>() {
      private int filaNro = 0;

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public Resultado next() {
        String[] row = iterator.next();
        filaNro++;
        try {
          Documento doc = finalDocPos != -1 && finalTipoDocPos != -1
              ? new Documento(TipoDocumento.valueOf(row[finalTipoDocPos]), row[finalDocPos])
              : null;

          var datos = new DatosDonante(
              doc,
              finalTipoPersonaPos != -1 ? row[finalTipoPersonaPos] : null,
              finalNombrePos != -1 ? row[finalNombrePos] : null,
              finalEmailPos != -1 ? new CorreoDeContato(row[finalEmailPos]) : null,
              finalTelPos != -1 ? new TelefonoDeContato(row[finalTelPos]) : null
          );
          return new Resultado(datos, false, filaNro);
        } catch (IllegalArgumentException | DomainValidationException e) {
          return new Resultado(null, true, filaNro);
        }
      }
    };
  }
}