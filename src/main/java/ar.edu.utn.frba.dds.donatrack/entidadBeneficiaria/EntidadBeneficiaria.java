package ar.edu.utn.frba.dds.donatrack.entidadBeneficiaria;

import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donacion.EstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.necesidades.NecesidadExtraordinaria;
import ar.edu.utn.frba.dds.donatrack.necesidades.NecesidadRecurrente;
import ar.edu.utn.frba.dds.donatrack.necesidades.Periodo;
import java.util.ArrayList;
import java.util.List;

public class EntidadBeneficiaria {
  private String razonSocial;
  private String direccion;
  //private List <MedioDeContacto> contactoRepresentantes;
  private List<Necesidad> necesidades;

  public EntidadBeneficiaria (String razon,
                              String direccion
                              //List <MedioDeContacto> contactoRepresentantes
                              ) {
    this.razonSocial = razon;
    this.direccion = direccion;
    //this.contactoRepresentantes = new ArrayList<>();
  }
  public void registrarNecesidad(Necesidad necesidad) {
    this.necesidades.add(necesidad);
  }
  public String getRazonSocial() {
    return this.razonSocial;
  }
}
