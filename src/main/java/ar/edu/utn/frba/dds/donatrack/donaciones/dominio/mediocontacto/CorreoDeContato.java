package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ClienteCorreo;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.ProveedorClienteCorreo;

public class CorreoDeContato extends MedioContacto {
  private String correo;
  private ClienteCorreo clienteCorreo;

  public CorreoDeContato(String correo) {
    if (!correo.matches("^.*@.*$")) {
      throw new DomainValidationException("Correo invalido");
    }
    this.correo = correo;
    this.esPrincipal = false;

    this.clienteCorreo = ProveedorClienteCorreo.getInstancia();
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