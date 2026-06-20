package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.Representante;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.MedioContacto;
import java.util.ArrayList;
import java.util.List;

public class PersonaJuridicaBuilder {
  private String razonSocial;
  private Documento documento;
  private TipoOrganizacion tipoOrganizacion;
  private String rubro;
  private List<Representante> representantes;
  private List<MedioContacto> contactos;

  public PersonaJuridicaBuilder() {
    this.representantes = new ArrayList<>();
    this.contactos = new ArrayList<>();
  }

  public PersonaJuridicaBuilder conRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
    return this;
  }

  public PersonaJuridicaBuilder conDocumento(Documento documento) {
    this.documento = documento;
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

  public PersonaJuridicaBuilder conContactoPrincipal(MedioContacto contacto) {
    contacto.setPrincipal(true);
    this.contactos.add(contacto);
    return this;
  }

  public PersonaJuridicaBuilder conContactoSecundario(MedioContacto contacto) {
    this.contactos.add(contacto);
    return this;
  }

  public PersonaJuridica build() {
    return new PersonaJuridica(razonSocial, tipoOrganizacion, rubro,
                              documento, representantes, contactos);
  }
}
