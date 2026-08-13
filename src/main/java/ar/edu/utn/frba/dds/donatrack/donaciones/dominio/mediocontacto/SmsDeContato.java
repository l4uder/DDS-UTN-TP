package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms.ClienteSmsMock;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.ValidacionDominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms.ClienteSms;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SmsDeContato implements MedioContacto {
  private String telefono;
  private Boolean esPrincipal;
  @Setter
  private ClienteSms clienteSms;

  public SmsDeContato(String telefono, Boolean esPrincipal) {
    checkDatos(telefono, esPrincipal);
    this.telefono = telefono;
    this.esPrincipal = esPrincipal;
    this.clienteSms = new ClienteSmsMock();
  }

  public SmsDeContato(String telefono, Boolean esPrincipal, ClienteSms clienteSms) {
    checkDatos(telefono, esPrincipal);
    this.telefono = telefono;
    this.esPrincipal = esPrincipal;
    this.clienteSms = clienteSms;
  }

  private void checkDatos(String telefono, Boolean estado) {
    if (telefono == null || telefono.isBlank()) {
      throw new ValidacionDominioException("Cada sms necesita un 'valor' valido");
    }
    if (!telefono.matches("^[+0-9 -]*$")) {
      throw new ValidacionDominioException("Teléfono invalido, verifique por favor");
    }
    if (estado == null) {
      throw new ValidacionDominioException("Cada contacto necesita 'principal' para saber si es un contacto principal o no");
    }
  }

  @Override
  public void notificar(String message) {
    if (clienteSms == null) {
      throw new ValidacionDominioException("clienteSms no asignado para enviar notificaciones");
    }

    clienteSms.enviarSms(telefono, message);
  }

  @Override
  public boolean esIgualA(MedioContacto otro) {
    if (!(otro instanceof SmsDeContato numeroSms)) {
      return false;
    }

    return this.telefono.equals(numeroSms.getTelefono());
  }
}
