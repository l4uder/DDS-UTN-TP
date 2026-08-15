package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

public interface MedioContacto {
  Boolean getEsPrincipal();
  void enviarMensaje(String message);
  boolean esIgualA(MedioContacto otro);
}
