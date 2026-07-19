package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo;

public class ClienteCorreoMock implements ClienteCorreo{
  @Override
  public void enviarCorreo(String correo, String mensaje) {
    System.out.println("Enviando: " + mensaje + " Al correo: " + correo);
  }
}
