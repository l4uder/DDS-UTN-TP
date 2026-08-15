package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms;

public class ClienteSmsMock implements ClienteSms {

  public ClienteSmsMock() { }

  @Override
  public void enviarSms(String numeroTelefono, String mensaje) {
    System.out.println("mensaje: " + mensaje + " enviado al sms: " + numeroTelefono);
  }

}