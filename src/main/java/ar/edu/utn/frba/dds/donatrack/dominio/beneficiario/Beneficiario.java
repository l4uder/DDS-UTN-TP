package ar.edu.utn.frba.dds.donatrack.dominio.beneficiario;

import ar.edu.utn.frba.dds.donatrack.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.dominio.necesidades.Necesidad;
import java.util.ArrayList;
import java.util.List;

public class Beneficiario {
  private String razonSocial;
  private String direccion;
  private List<MedioContacto> contactos;
  private List<Necesidad> necesidades;
  //Doble asociacion bidericcional
  private List<Donacion> donaciones;

  public Beneficiario(String razon,
                      String direccion,
                      List<MedioContacto> contactos) {
    this.razonSocial = razon;
    this.direccion = direccion;
    this.contactos = new ArrayList<>(contactos);
    this.necesidades = new ArrayList<>();
    this.donaciones = new ArrayList<>();
  }

  public String getRazonSocial() {
    return this.razonSocial;
  }

  public List<Necesidad> getNecesidades() {
    return this.necesidades;
  }

  public List<Donacion> getDonaciones() {
    return this.donaciones;
  }

  public void agregarNecesidad(Necesidad necesidad) {
    this.necesidades.add(necesidad);
  }

  public void asignarDonacion(Donacion donacion) {
    if (donacion.getEstadoActual() != TipoEstadoDonacion.ASIGNACION_REALIZADA) {
      throw new DomainValidationException("Al beneficiario le debe llegar donaciones asignadas");
    }
    this.donaciones.add(donacion);
  }

  public Boolean esIgual(Beneficiario otroBeneficiario) {
    return razonSocial.equalsIgnoreCase(otroBeneficiario.getRazonSocial());
  }
}