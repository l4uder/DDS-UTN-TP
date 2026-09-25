package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp.ClienteWhatsappMock;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp.ClienteWhatsappRealTwilio;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp.ClienteWhatsapp;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("whatsapp")
public class WhatsappDeContacto extends MedioContacto {
  @Transient
  private ClienteWhatsapp clienteWhatsapp;

  public WhatsappDeContacto(String telefono, Boolean esPrincipal) {
    checkDatos(telefono, esPrincipal);
    this.detalle = telefono;
    this.esPrincipal = esPrincipal;
    this.clienteWhatsapp = new ClienteWhatsappMock();
  }

  public WhatsappDeContacto(String telefono, Boolean esPrincipal, ClienteWhatsapp clienteWhatsapp) {
    checkDatos(telefono, esPrincipal);
    this.detalle = telefono;
    this.esPrincipal = esPrincipal;
    this.clienteWhatsapp = clienteWhatsapp;
  }

  private void checkDatos(String telefono, Boolean estado) {
    if (telefono == null || telefono.isBlank()) {
      throw new DominioException("Cada whatsapp necesita un 'valor' valido");
    }
    if (!telefono.matches("^[+0-9 -]*$")) {
      throw new DominioException("Teléfono invalido");
    }
    if (estado == null) {
      throw new DominioException("Cada contacto necesita 'principal' para saber si es un contacto principal o no");
    }
  }

  @Override
  public void enviarMensaje(String message) {
    if (this.clienteWhatsapp == null) {
      this.clienteWhatsapp = new ClienteWhatsappRealTwilio();
    }
    clienteWhatsapp.enviarWhatsapp(detalle, message);
  }

  @Override
  public boolean esIgualA(MedioContacto otro) {
    if (!(otro instanceof WhatsappDeContacto numeroWhatsapp)) {
      return false;
    }
    return this.detalle.equals(numeroWhatsapp.getDetalle());
  }

}
