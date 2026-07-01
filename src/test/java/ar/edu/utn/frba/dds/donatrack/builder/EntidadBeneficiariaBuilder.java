package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.dominio.necesidades.Necesidad;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class EntidadBeneficiariaBuilder {
  private String razonSocial;
  private String direccion;
  private List<MedioContacto> contactoRepresentantes;
  private List<Necesidad> necesidades;

  public EntidadBeneficiariaBuilder(){
    this.necesidades = new ArrayList<>();
  }

  public EntidadBeneficiariaBuilder conRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
    return this;
  }

  public EntidadBeneficiariaBuilder conDireccion(String direccion) {
    this.direccion = direccion;
    return this;
  }

  public EntidadBeneficiariaBuilder conMediosContactos(List<MedioContacto> medioContactos) {
    this.contactoRepresentantes = medioContactos;
    return this;
  }

  public Beneficiario build(){
    return new Beneficiario(razonSocial, direccion, contactoRepresentantes);
  }
}
