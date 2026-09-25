package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp;

public interface ClienteWhatsapp {
  void enviarWhatsapp(String numeroWhatsapp, String mensaje);
}
