package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms.ClienteSmsMock;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms.ClienteSms;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("sms")
public class SmsDeContacto extends MedioContacto {
  @Transient
  private ClienteSms clienteSms;

  public SmsDeContacto(String telefono, Boolean esPrincipal) {
    checkDatos(telefono, esPrincipal);
    this.detalle = telefono;
    this.esPrincipal = esPrincipal;
    this.clienteSms = new ClienteSmsMock();
  }

  public SmsDeContacto(String telefono, Boolean esPrincipal, ClienteSms clienteSms) {
    checkDatos(telefono, esPrincipal);
    this.detalle = telefono;
    this.esPrincipal = esPrincipal;
    this.clienteSms = clienteSms;
  }

  private void checkDatos(String telefono, Boolean estado) {
    if (telefono == null || telefono.isBlank()) {
      throw new DominioException("Cada sms necesita un 'valor' valido");
    }
    if (!telefono.matches("^[+0-9 -]*$")) {
      throw new DominioException("Teléfono invalido, verifique por favor");
    }
    if (estado == null) {
      throw new DominioException("Cada contacto necesita 'principal' para saber si es un contacto principal o no");
    }
  }

  public String getDetalle() {
    return this.detalle;
  }

  @Override
  public void enviarMensaje(String message) {
    clienteSms.enviarSms(detalle, message);
  }

  @Override
  public boolean esIgualA(MedioContacto otro) {
    if (!(otro instanceof SmsDeContacto numeroSms)) {
      return false;
    }

    return this.detalle.equals(numeroSms.getDetalle());
  }

}
