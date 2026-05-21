package ar.edu.utn.frba.dds.donatrack.donante;

import java.util.ArrayList;
import java.util.List;

public class PersonaJuridica extends Donante {
  private String razonSocial;
  private TipoOrganizacion tipo;
  private String rubro;
  private List<Representante> representantes;

  public PersonaJuridica(String razonSocial, TipoOrganizacion tipo,
                         String rubro, List<Representante> representantes,
                         MedioContacto medioContPred, List<MedioContacto> contactos) {
    super(medioContPred, contactos);
    this.razonSocial = razonSocial;
    this.tipo = tipo;
    this.rubro = rubro;
    this.representantes = new ArrayList<>(representantes);
  }
}
