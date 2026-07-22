package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp.ClienteWhatsappMock;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp.ClienteWhatsapp;

public class WhatsappDeContato implements MedioContacto {
  private String telefono;
  private Boolean esPrincipal;
  private ClienteWhatsapp clienteWhatsapp;

  public WhatsappDeContato(String telefono, Boolean esPrincipal) {
    checkDatos(telefono, esPrincipal);
    this.telefono = telefono;
    this.esPrincipal = esPrincipal;
    this.clienteWhatsapp = new ClienteWhatsappMock();
  }

  private void checkDatos(String telefono, Boolean estado) {
    if (!telefono.matches("^[+0-9 -]*$")) {
      throw new DomainValidationException("Telefono invalido");
    }
    if (estado == null) {
      throw new DomainValidationException("Debe indicar si es un contacto principal o no");
    }
  }

  @Override
  public Boolean getEsPrincipal() {
    return this.esPrincipal;
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
