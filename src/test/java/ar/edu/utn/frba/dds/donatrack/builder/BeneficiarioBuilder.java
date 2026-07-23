package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import java.util.ArrayList;
import java.util.List;

public class BeneficiarioBuilder {
  private String razonSocial;
  private String direccion;
  private List<MedioContacto> contactos;
  private List<Necesidad> necesidades;

  public BeneficiarioBuilder(){
    this.contactos = new ArrayList<>();
    this.necesidades = new ArrayList<>();
  }

  public BeneficiarioBuilder conRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
    return this;
  }

  public BeneficiarioBuilder conDireccion(String direccion) {
    this.direccion = direccion;
    return this;
  }

  public BeneficiarioBuilder conAgregarContacto(MedioContacto medioContacto) {
    this.contactos.add(medioContacto);
    return this;
  }

  public Beneficiario build(){
    return new Beneficiario(razonSocial, direccion, contactos);
  }
}
