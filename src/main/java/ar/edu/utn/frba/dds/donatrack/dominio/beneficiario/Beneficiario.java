package ar.edu.utn.frba.dds.donatrack.dominio.beneficiario;

import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.dominio.necesidades.Necesidad;
import java.util.ArrayList;
import java.util.List;

public class Beneficiario {
  private String razonSocial;
  private String direccion;
  private List<MedioContacto> contactos;
  private List<Necesidad> necesidades;

  public Beneficiario(String razon,
                      String direccion,
                      List<MedioContacto> contactos) {
    this.razonSocial = razon;
    this.direccion = direccion;
    this.contactos = new ArrayList<>(contactos);
    this.necesidades = new ArrayList<>();
  }

  public void registrarNecesidad(Necesidad necesidad) {
    this.necesidades.add(necesidad);
  }

  public String getRazonSocial() {
    return this.razonSocial;
  }
}