package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.TipoOrganizacion;
import java.util.ArrayList;
import java.util.List;

public class PersonaJuridicaBuilder {
  private String razonSocial;
  private Documento documento;
  private TipoOrganizacion tipoOrganizacion;
  private String rubro;
  private List<Representante> representantes;

  public PersonaJuridicaBuilder() {
    this.representantes = new ArrayList<>();
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

  public PersonaJuridicaBuilder conAgregarRepresetante(Representante representante) {
    this.representantes.add(representante);
    return this;
  }

  public Donante build() {
    return Donante.personaJuridica(razonSocial, documento, tipoOrganizacion, rubro, representantes);
  }
}
