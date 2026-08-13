package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo.ClienteCorreoMock;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.ValidacionDominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo.ClienteCorreo;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CorreoDeContato implements MedioContacto {
  private String correo;
  private Boolean esPrincipal;
  @Setter
  private ClienteCorreo clienteCorreo;

  public CorreoDeContato(String correo, Boolean esPrincipal) {
    checkDatos(correo, esPrincipal);
    this.correo = correo;
    this.esPrincipal = esPrincipal;
    this.clienteCorreo = new ClienteCorreoMock();
  }

  public CorreoDeContato(String correo, Boolean esPrincipal, ClienteCorreo clienteCorreo) {
    checkDatos(correo, esPrincipal);
    this.correo = correo;
    this.esPrincipal = esPrincipal;
    this.clienteCorreo = clienteCorreo;
  }

  private void checkDatos(String correo, Boolean estado) {
    if (correo == null || correo.isBlank()) {
      throw new ValidacionDominioException("Cada correo necesita un 'valor' valido");
    }
    if (!correo.matches("^.*@.*$")) {
      throw new ValidacionDominioException("Correo invalido, verifique por favor");
    }
    if (estado == null) {
      throw new ValidacionDominioException("Cada contacto necesita 'principal' para saber si es un contacto principal o no");
    }
  }

  @Override
  public void notificar(String message) {
    if (clienteCorreo == null) {
      throw new ValidacionDominioException("clienteCorreo no asignado para enviar notificaciones");
    }

    clienteCorreo.enviarCorreo(correo, message);
  }

  @Override
  public boolean esIgualA(MedioContacto otro) {
    if (!(otro instanceof CorreoDeContato correoElectronico)) {
      return false;
    }

    return this.correo.equalsIgnoreCase(correoElectronico.getCorreo());
  }
}