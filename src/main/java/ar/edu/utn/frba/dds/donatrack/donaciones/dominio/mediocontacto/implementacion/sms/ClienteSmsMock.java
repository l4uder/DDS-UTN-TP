package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms;

public class ClienteSmsMock implements ClienteSms {
  @Override
  public void enviarSms(String numeroTelefono, String mensaje) {
    System.out.println("Enviando: " + mensaje + " Al numero de sms: " + numeroTelefono);
  }
}
