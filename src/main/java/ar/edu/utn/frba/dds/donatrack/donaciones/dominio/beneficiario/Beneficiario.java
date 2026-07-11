package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Beneficiario {
  private String id;
  private String razonSocial;
  private String direccion;
  private List<MedioContacto> contactos;
  private List<Necesidad> necesidades;
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

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRazonSocial() {
    return this.razonSocial;
  }

  public String getDireccion() {
    return this.direccion;
  }

  public List<MedioContacto> getContactos() {
    return new ArrayList<>(this.contactos);
  }

  public void actualizarDatos(String razonSocial,
                              String direccion,
                              List<MedioContacto> contactos) {
    this.razonSocial = razonSocial;
    this.direccion = direccion;
    this.contactos = new ArrayList<>(contactos);
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

  public Optional<Necesidad> buscarNecesidad(String necesidadId) {
    return this.necesidades.stream()
        .filter(necesidad -> necesidadId.equals(necesidad.getId()))
        .findFirst();
  }

  public void eliminarNecesidad(Necesidad necesidad) {
    this.necesidades.remove(necesidad);
  }

  public void asignarDonacion(Donacion donacion) {
    if (donacion.getEstadoActual() != TipoEstadoDonacion.ASIGNACION_REALIZADA) {
      throw new DomainValidationException("Al beneficiario le debe llegar donaciones asignadas");
    }
    this.donaciones.add(donacion);
  }

  public void recibirNotificacion(String mensaje) {
    List<MedioContacto> contactos = getContactos();
    contactos.forEach(c -> c.notificar(mensaje));
  }

  public Boolean esIgual(Beneficiario otroBeneficiario) {
    return razonSocial.equalsIgnoreCase(otroBeneficiario.getRazonSocial());
  }

  public MedioContacto getContactoPrincipal() {
    if (this.contactos != null && !this.contactos.isEmpty()) {
        return this.contactos.get(0);
    }
    throw new DomainValidationException("El beneficiario no posee contactos");
}
}