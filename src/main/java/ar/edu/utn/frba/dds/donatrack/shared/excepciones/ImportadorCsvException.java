package ar.edu.utn.frba.dds.donatrack.shared.excepciones;

public class ImportadorCsvException extends RuntimeException {

  public ImportadorCsvException(String message, Throwable cause) {
    super(message, cause);
  }

  public ImportadorCsvException(String message) {
    super(message);
  }

}