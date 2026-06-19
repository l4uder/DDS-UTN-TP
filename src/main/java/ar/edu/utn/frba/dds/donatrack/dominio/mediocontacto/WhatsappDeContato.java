package ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto;

import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.implementacion.ClienteWhatsapp;

public class WhatsappDeContato extends MedioContacto {
  private String telefono;
  private ClienteWhatsapp clienteWhatsapp;

  public WhatsappDeContato(String telefono) {
    if (!telefono.matches("^[+0-9 -]*$")) {
      throw new DomainValidationException("Telefono invalido");
    }
    this.telefono = telefono;
    this.esPrincipal = false;
  }

  @Override
  public void notificar(String message) {
    if (clienteWhatsapp == null) {
      throw new DomainValidationException("clienteWhatsapp no asignado para enviar notificaciones");
    }

    clienteWhatsapp.enviarMensaje(telefono, message);
  }

  public void setClienteWhatsapp(ClienteWhatsapp clienteWhatsapp) {
    this.clienteWhatsapp = clienteWhatsapp;
  }

  public String getTelefono() {
    return telefono;
  }

  @Override
  public boolean esIgualA(MedioContacto otro) {
    if (!(otro instanceof WhatsappDeContato numeroWhatsapp)) {
      return false;
    }

    return this.telefono.equals(numeroWhatsapp.getTelefono());
  }
}
