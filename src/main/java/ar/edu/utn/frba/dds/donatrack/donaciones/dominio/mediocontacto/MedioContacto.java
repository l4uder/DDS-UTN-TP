package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

public interface MedioContacto {
  Boolean getEsPrincipal();
  void notificar(String message);
  boolean esIgualA(MedioContacto otro);

}
