package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo.ClienteCorreoMock;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo.ClienteCorreo;

public class CorreoDeContato implements MedioContacto {
  private String correo;
  private Boolean esPrincipal;
  private ClienteCorreo clienteCorreo;

  public CorreoDeContato(String correo, Boolean esPrincipal) {
    checkDatos(correo, esPrincipal);
    this.correo = correo;
    this.esPrincipal = esPrincipal;
    this.clienteCorreo = new ClienteCorreoMock();
  }

  private void checkDatos(String correo, Boolean estado) {
    if (!correo.matches("^.*@.*$")) {
      throw new DomainValidationException("Correo invalido, verifique por favor");
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
    if (clienteCorreo == null) {
      throw new DomainValidationException("clienteCorreo no asignado para enviar notificaciones");
    }

    clienteCorreo.enviarCorreo(correo, message);
  }

  public void setClienteCorreo(ClienteCorreo clienteCorreo) {
    this.clienteCorreo = clienteCorreo;
  }

  public String getCorreo() {
    return correo;
  }

  @Override
  public boolean esIgualA(MedioContacto otro) {
    if (!(otro instanceof CorreoDeContato correoElectronico)) {
      return false;
    }

    return this.correo.equalsIgnoreCase(correoElectronico.getCorreo());
  }
}