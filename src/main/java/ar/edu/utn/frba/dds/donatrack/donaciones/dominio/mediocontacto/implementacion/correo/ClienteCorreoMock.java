package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo;

public class ClienteCorreoMock implements ClienteCorreo{
  @Override
  public void enviarCorreo(String correoDestino, String mensaje) {
    System.out.println("mensaje: " + mensaje + " enviado al correo: " + correoDestino);
  }
}
