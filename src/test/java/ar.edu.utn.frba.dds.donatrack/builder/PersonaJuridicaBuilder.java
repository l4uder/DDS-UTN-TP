package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donante.Representante;
import ar.edu.utn.frba.dds.donatrack.donante.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import java.util.ArrayList;
import java.util.List;

public class PersonaJuridicaBuilder {
  private String razonSocial = "Constructora SRL";
  private TipoOrganizacion tipoOrganizacion = TipoOrganizacion.EMPRESA;
  private String rubro = "Construcción";
  private List<Representante> representantes = new ArrayList<>(List.of(new RepresentanteBuilder().build()));
  private MedioContacto medioContPred = new MedioContacto(TipoContacto.CORREO, "srl@gmail.com");
  private List<MedioContacto> contactosSecundarios = new ArrayList<>();

  public PersonaJuridicaBuilder conRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
    return this;
  }

  public PersonaJuridicaBuilder conTipo(TipoOrganizacion tipoOrganizacion) {
    this.tipoOrganizacion = tipoOrganizacion;
    return this;
  }

  public PersonaJuridicaBuilder conRubro(String rubro) {
    this.rubro = rubro;
    return this;
  }

  public PersonaJuridicaBuilder conRepresentantes(List<Representante> representantes) {
    this.representantes = representantes;
    return this;
  }

  public PersonaJuridicaBuilder conMedioDeContacto(MedioContacto medioContacto) {
    this.medioContPred = medioContacto;
    return this;
  }

  public PersonaJuridica build() {
    return new PersonaJuridica(razonSocial, tipoOrganizacion, rubro, representantes, medioContPred, contactosSecundarios);
  }
}