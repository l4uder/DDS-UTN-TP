package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.contacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donante.*;
import ar.edu.utn.frba.dds.donatrack.contacto.MedioContacto;
import java.util.ArrayList;
import java.util.List;

public class PersonaJuridicaBuilder {
  private String razonSocial = "Empresa Default SA";
  private Documento documento = new Documento(TipoDocumento.CUIT, "235345354");
  private TipoOrganizacion tipoOrganizacion = TipoOrganizacion.EMPRESA;
  private String rubro = "Rubro Default";
  private List<Representante> representantes = new ArrayList<>();
  private MedioContacto medioContPred = new CorreoDeContato("default@empresa.com");
  private List<MedioContacto> contactosSecundarios = new ArrayList<>();

  public PersonaJuridicaBuilder conRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
    return this;
  }

  public PersonaJuridicaBuilder conTipoOrganizacion(TipoOrganizacion tipo) {
    this.tipoOrganizacion = tipo;
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

  public PersonaJuridicaBuilder conEmail(String email) {
    this.medioContPred = new CorreoDeContato(email);
    return this;
  }

  public PersonaJuridicaBuilder conContactosSecundarios(List<MedioContacto> contactos) {
    this.contactosSecundarios = contactos;
    return this;
  }

  public PersonaJuridica build() {
    return new PersonaJuridica(razonSocial, tipoOrganizacion, rubro, documento,
        representantes, medioContPred, contactosSecundarios);
  }
}
