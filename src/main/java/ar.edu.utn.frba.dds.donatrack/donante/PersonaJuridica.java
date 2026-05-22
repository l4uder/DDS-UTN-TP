package ar.edu.utn.frba.dds.donatrack.donante;

import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import java.util.ArrayList;
import java.util.List;

public class PersonaJuridica extends Donante {
  private String razonSocial;
  private TipoOrganizacion tipoOrganizacion;
  private String rubro;
  private List<Representante> representantes;

  public PersonaJuridica(
      String razonSocial,
      TipoOrganizacion tipo,
      String rubro,
      List<Representante> representantes,
      MedioContacto medioContPred,
      List<MedioContacto> contactosSecundarios) {
    super(medioContPred, contactosSecundarios);
    /*if (representantes == null || representantes.isEmpty()) {
      throw new IllegalArgumentException("La persona jurídica debe tener al menos un representante");
    } */ //EL CSV no tiene informacion de los representantes
    this.razonSocial = razonSocial;
    this.tipoOrganizacion = tipo;
    this.rubro = rubro;
    this.representantes = representantes == null ? new ArrayList<>() : new ArrayList<>(representantes);
  }
}
