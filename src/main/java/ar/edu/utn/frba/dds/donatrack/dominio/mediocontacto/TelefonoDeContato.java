package ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto;

import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.implementacion.ClienteSms;

public class TelefonoDeContato implements MedioContacto {
  protected String telefono;
  private ClienteSms clienteSms;

  public TelefonoDeContato(String telefono) {
    if (!telefono.matches("^[+0-9 -]*$")) {
      throw new DomainValidationException("Telefono invalido");
    }
    this.telefono = telefono;
  }

  @Override
  public void notificar(String message) {
    clienteSms.enviarSms(telefono, message);
  }

  public void setClienteSms(ClienteSms clienteSms) {
    this.clienteSms = clienteSms;
  }

  public String getTelefono() {
    return telefono;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof TelefonoDeContato contacto
        && contacto.telefono.equalsIgnoreCase(telefono);
  }

  @Override
  public int hashCode() {
    return telefono.hashCode();
  }
}
