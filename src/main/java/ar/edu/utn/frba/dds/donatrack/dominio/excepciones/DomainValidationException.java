package ar.edu.utn.frba.dds.donatrack.dominio.excepciones;

public class DomainValidationException extends RuntimeException {
    public DomainValidationException(String message) {
        super(message);
    }
}
