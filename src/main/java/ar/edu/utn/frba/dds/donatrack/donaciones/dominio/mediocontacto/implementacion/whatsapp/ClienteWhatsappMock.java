package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp;

public class ClienteWhatsappMock implements ClienteWhatsapp{
  @Override
  public void enviarMensaje(String numeroWhatsapp, String mensaje) {
    System.out.println("Enviando: " + mensaje + " Al numero de whatsapp: " + numeroWhatsapp);
  }
}
