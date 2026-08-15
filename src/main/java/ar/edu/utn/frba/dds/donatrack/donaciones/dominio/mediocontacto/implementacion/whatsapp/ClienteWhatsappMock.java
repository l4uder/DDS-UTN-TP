package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp;

public class ClienteWhatsappMock implements ClienteWhatsapp {

  public ClienteWhatsappMock() { }

  @Override
  public void enviarWhatsapp(String numeroWhatsapp, String mensaje) {
    System.out.println("mensaje: " + mensaje + " enviado al whatsapp: " + numeroWhatsapp);
  }

}