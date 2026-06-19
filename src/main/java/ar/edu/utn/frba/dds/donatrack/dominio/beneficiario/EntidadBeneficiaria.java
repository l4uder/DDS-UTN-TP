package ar.edu.utn.frba.dds.donatrack.dominio.beneficiario;

import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.dominio.necesidades.Necesidad;
import java.util.ArrayList;
import java.util.List;

public class EntidadBeneficiaria {
  private String razonSocial;
  private String direccion;
  private List<MedioContacto> contactoRepresentantes;
  private List<Necesidad> necesidades;

  public EntidadBeneficiaria(String razon,
                             String direccion,
                             List<MedioContacto> contactoRepresentantes) {
    this.razonSocial = razon;
    this.direccion = direccion;
    this.contactoRepresentantes = new ArrayList<>(contactoRepresentantes);
    this.necesidades = new ArrayList<>();
  }

  public void registrarNecesidad(Necesidad necesidad) {
    this.necesidades.add(necesidad);
  }

  public String getRazonSocial() {
    return this.razonSocial;
  }
}