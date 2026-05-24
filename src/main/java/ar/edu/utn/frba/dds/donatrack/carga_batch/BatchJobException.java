package ar.edu.utn.frba.dds.donatrack.carga_batch;

public class BatchJobException extends RuntimeException {
    public BatchJobException(String message, Throwable cause) {
        super(message, cause);
    }
    public BatchJobException(String message) {
        super(message);
    }
}
