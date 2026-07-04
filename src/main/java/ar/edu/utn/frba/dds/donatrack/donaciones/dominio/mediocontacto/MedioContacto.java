package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

public abstract class MedioContacto {
  protected Boolean esPrincipal;

  public Boolean getPrincipal() {
    return this.esPrincipal;
  }

  public void setPrincipal(Boolean esPrincipal) {
    this.esPrincipal = esPrincipal;
  }

  public abstract void notificar(String message);

  public abstract boolean esIgualA(MedioContacto otro);
}
