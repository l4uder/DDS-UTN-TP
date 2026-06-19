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
  private String razonSocial = "Empresa Default SA";
  private Documento documento = new Documento(TipoDocumento.CUIT, "235345354");
  private TipoOrganizacion tipoOrganizacion = TipoOrganizacion.EMPRESA;
  private String rubro = "Rubro Default";
  private List<Representante> representantes = new ArrayList<>();
  private List<MedioContacto> contactos = new ArrayList<>();

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
    CorreoDeContato correo = new CorreoDeContato(email);
    correo.setPrincipal(true);
    this.contactos.add(correo);
    return this;
  }

  public PersonaJuridicaBuilder conContactosSecundarios(List<MedioContacto> secundarios) {
    if (secundarios != null) {
      secundarios.forEach(c -> c.setPrincipal(false));
      this.contactos.addAll(secundarios);
    }
    return this;
  }

  public PersonaJuridica build() {
    return new PersonaJuridica(razonSocial, tipoOrganizacion, rubro,
                              documento, representantes, contactos);
  }
}
