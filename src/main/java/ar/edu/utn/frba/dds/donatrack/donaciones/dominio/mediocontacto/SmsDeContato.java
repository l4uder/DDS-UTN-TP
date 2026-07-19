package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms.ClienteSmsMock;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms.ClienteSms;

public class SmsDeContato extends MedioContacto {
  private String telefono;
  private ClienteSms clienteSms;

  public SmsDeContato(String telefono) {
    if (!telefono.matches("^[+0-9 -]*$")) {
      throw new DomainValidationException("Telefono invalido");
    }
    this.telefono = telefono;
    this.esPrincipal = false;
    this.clienteSms = new ClienteSmsMock();
  }

  @Override
  public void notificar(String message) {
    if (clienteSms == null) {
      throw new DomainValidationException("clienteSms no asignado para enviar notificaciones");
    }

    clienteSms.enviarSms(telefono, message);
  }

  public void setClienteSms(ClienteSms clienteSms) {
    this.clienteSms = clienteSms;
  }

  public String getTelefono() {
    return telefono;
  }

  @Override
  public boolean esIgualA(MedioContacto otro) {
    if (!(otro instanceof SmsDeContato numeroSms)) {
      return false;
    }

    return this.telefono.equals(numeroSms.getTelefono());
  }
}
