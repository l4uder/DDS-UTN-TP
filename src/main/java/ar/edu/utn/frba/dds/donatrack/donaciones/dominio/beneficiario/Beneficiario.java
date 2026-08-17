package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Beneficiario {
  @Setter
  private String id;
  private String razonSocial;
  private String direccion;
  private List<MedioContacto> contactos;
  private List<Necesidad> necesidades;
  private List<Donacion> donaciones;

  public Beneficiario(String razonSocial, String direccion, List<MedioContacto> contactos) {
    checkDatos(razonSocial, contactos);
    this.razonSocial = razonSocial;
    this.direccion = direccion;
    this.contactos = new ArrayList<>(contactos);
    this.necesidades = new ArrayList<>();
    this.donaciones = new ArrayList<>();
  }

  private void checkDatos(String razonSocial, List<MedioContacto> contactos) {
    if (razonSocial == null || razonSocial.isBlank()) {
      throw new DominioException("El campo 'razon_social' es obligatorio, en el Beneficiario");
    }
    if (contactos == null || contactos.isEmpty()) {
      throw new DominioException( "Debe tener al menos un medio de contacto, en el Beneficiario");
    }
    if (contactos.stream().noneMatch(MedioContacto::getEsPrincipal)) {
      throw new DominioException("Debe tener al menos un contacto principal, en el beneficiario");
    }
  }

  public void agregarNecesidad(Necesidad necesidad) {
    this.necesidades.add(necesidad);
  }

  public Necesidad buscarNecesidadPorId(String necesidadId) {
    return this.necesidades.stream()
        .filter(necesidad -> necesidadId.equals(necesidad.getId()))
        .findFirst()
        .orElseThrow(() -> new RecursoNoEncontradoException("El beneficiario no tiene una necesidad con ese id " + necesidadId));
  }

  public void eliminarNecesidadPorId(String necesidadId) {
    Necesidad necesidad = buscarNecesidadPorId(necesidadId);
    this.necesidades.remove(necesidad);
  }

  public void recibirDonacion(Donacion donacion) {
    if (donacion.getEstadoActual() != TipoEstadoDonacion.ASIGNACION_REALIZADA) {
      throw new DominioException("Al beneficiario le debe llegar donaciones asignadas");
    }
    this.donaciones.add(donacion);
  }

  public void recibirNotificacion(String mensaje) {
    List<MedioContacto> contactosPrincipales = getContactos().stream().filter(c -> c.getEsPrincipal()).toList();
    contactosPrincipales.forEach(c -> c.enviarMensaje(mensaje));
  }

  public void recibirNotificacionImportante(String mensaje) {
    List<MedioContacto> contactos = getContactos();
    contactos.forEach(c -> c.enviarMensaje(mensaje));
  }

  public Boolean esIgual(Beneficiario otroBeneficiario) {
    return razonSocial.equalsIgnoreCase(otroBeneficiario.getRazonSocial());
  }

  public void actualizarDatos(String razonSocial, String direccion, List<MedioContacto> contactos) {
    checkDatos(razonSocial, contactos);
    this.razonSocial = razonSocial;
    this.direccion = direccion;
    this.contactos = new ArrayList<>(contactos);
  }

}