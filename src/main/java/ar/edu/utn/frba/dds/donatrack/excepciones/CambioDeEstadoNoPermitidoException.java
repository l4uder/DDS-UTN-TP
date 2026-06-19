package ar.edu.utn.frba.dds.donatrack.excepciones;

public class CambioDeEstadoNoPermitidoException extends RuntimeException {
    public CambioDeEstadoNoPermitidoException(String message) {
        super(message);
    }
}
