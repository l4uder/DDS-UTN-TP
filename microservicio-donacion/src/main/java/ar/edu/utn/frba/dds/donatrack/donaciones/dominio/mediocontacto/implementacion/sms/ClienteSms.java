package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms;

public interface ClienteSms {
  void enviarSms(String numeroTelefono, String mensaje);
}
