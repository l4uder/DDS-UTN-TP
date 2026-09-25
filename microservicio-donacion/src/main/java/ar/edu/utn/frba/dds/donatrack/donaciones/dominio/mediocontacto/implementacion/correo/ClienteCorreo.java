package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo;

public interface ClienteCorreo {
  void enviarCorreo(String correoDestino, String mensaje);
}
