package ar.edu.utn.frba.dds.donatrack.dominio.medioContacto;

import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.dominio.medioContacto.implementacion.ClienteCorreo;

public class CorreoDeContato implements MedioContacto {

    private String correo;
    private ClienteCorreo clienteCorreo;

    public CorreoDeContato(String correo) {
        if (!correo.matches("^.*@.*$")) {
            throw new DomainValidationException("Correo invalido");
        }
        this.correo = correo;
    }

    @Override
    public void notificar(String message) {
        clienteCorreo.enviarCorreo(correo, message);
    }

    public void setClienteCorreo(ClienteCorreo clienteCorreo) {
        this.clienteCorreo = clienteCorreo;
    }

    public String getCorreo() {
        return correo;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CorreoDeContato contacto && contacto.correo.equalsIgnoreCase(correo);
    }

    @Override
    public int hashCode() {
        return correo.hashCode();
    }
}
