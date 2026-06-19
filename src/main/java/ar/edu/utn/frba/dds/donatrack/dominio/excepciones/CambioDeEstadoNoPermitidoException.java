package ar.edu.utn.frba.dds.donatrack.dominio.excepciones;

public class CambioDeEstadoNoPermitidoException extends RuntimeException {
    public CambioDeEstadoNoPermitidoException(String message) {
        super(message);
    }
}
