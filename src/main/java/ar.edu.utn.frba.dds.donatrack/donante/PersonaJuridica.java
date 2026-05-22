package ar.edu.utn.frba.dds.donatrack.donante;

import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;

import java.util.ArrayList;
import java.util.List;

public class PersonaJuridica extends Donante {
  private String razonSocial;
  private TipoOrganizacion tipoOrganizacion;
  private String rubro;
  private Documento documento;
  private List<Representante> representantes;

  public PersonaJuridica(
      String razonSocial,
      TipoOrganizacion tipo,
      String rubro,
      Documento documento,
      List<Representante> representantes,
      MedioContacto medioContPred,
      List<MedioContacto> contactosSecundarios) {
    super(medioContPred, contactosSecundarios);
    /*if (representantes == null || representantes.isEmpty()) {
      throw new IllegalArgumentException("La persona jurídica debe tener al menos un representante");
    } */ //EL CSV no tiene informacion de los representantes
    this.razonSocial = razonSocial;
    this.tipoOrganizacion = tipo;
    this.documento = documento;
    this.rubro = rubro;
    this.representantes = representantes == null ? new ArrayList<>() : new ArrayList<>(representantes);
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public TipoOrganizacion getTipoOrganizacion() {
    return tipoOrganizacion;
  }

  public String getRubro() {
    return rubro;
  }

  public List<Representante> getRepresentantes() {
    return representantes;
  }

  @Override
  public boolean esElMismo(Donante otroDonante) {
    if (!(otroDonante instanceof PersonaJuridica))
          return false;
    PersonaJuridica otraPersona =  (PersonaJuridica) otroDonante;

    return this.razonSocial.equalsIgnoreCase(otraPersona.razonSocial) &&
            this.getMediosContacto(TipoContacto.CORREO).stream().anyMatch(c-> c.getDetalle().equals(otraPersona.getMedioDeContactoPred().getDetalle()));
  }

  @Override
  public void actualizar(Donante otroDonante) {
    if (!(otroDonante instanceof PersonaJuridica))
          return;
    PersonaJuridica otraPersona =  (PersonaJuridica) otroDonante;

    this.razonSocial = otraPersona.getRazonSocial();
    this.tipoOrganizacion = otraPersona.getTipoOrganizacion();
    this.rubro = otraPersona.getRubro();
    this.representantes = otraPersona.getRepresentantes();
    this.representantes = otraPersona.getRepresentantes();
    this.medioDeContactoPred = otraPersona.getMedioDeContactoPred();
    this.contactosSecundarios = otraPersona.getContactosSecundarios();
  }

  @Override
  public String toString() {
      return "PersonaJuridica{" +
                "razonSocial: " + razonSocial  +
                ", rubro: " + rubro +
                '}';
  }
}
