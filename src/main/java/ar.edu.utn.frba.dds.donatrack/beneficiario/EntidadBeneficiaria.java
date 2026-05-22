package ar.edu.utn.frba.dds.donatrack.beneficiario;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donacion.EstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.necesidades.NecesidadExtraordinaria;
import ar.edu.utn.frba.dds.donatrack.necesidades.NecesidadRecurrente;
import ar.edu.utn.frba.dds.donatrack.necesidades.Periodo;
import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
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
