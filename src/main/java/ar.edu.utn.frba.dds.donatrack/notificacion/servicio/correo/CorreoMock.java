package ar.edu.utn.frba.dds.donatrack.notificacion.servicio.correo;

public class CorreoMock implements ServicioCorreo {
  @Override
  public void notificar(String correo, String mensaje) {
    System.out.println("Se envio un correo a: " + correo + " con el siguiente: " + mensaje);
  }
}
