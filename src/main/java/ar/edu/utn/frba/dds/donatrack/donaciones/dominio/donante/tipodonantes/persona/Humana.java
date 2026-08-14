package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.persona;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoPersona;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.TipoDonante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Humana implements TipoDonante {
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
  private Genero genero;
  private String direccion;
  private List<MedioContacto> contactos;

  public Humana(String nombre, String apellido, Documento documento,
                LocalDate fechaNacimiento, Genero genero, String direccion,
                List<MedioContacto> contactos) {
    checkDatos(nombre, documento, direccion, contactos);
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.genero = genero == null ? Genero.SIN_ESPECIFICAR : genero;
    this.direccion = direccion;
    this.contactos = new ArrayList<>(contactos);
  }

  private void checkDatos(String nombre, Documento documento, String direccion, List<MedioContacto> contactos) {
    if (nombre == null || nombre.isBlank()) {
      throw new DominioException("El campo 'nombre' es obligatorio, en la persona humana");
    }
    if (documento == null) {
      throw new DominioException("El documento es obligatorio, en la persona humana");
    }
    if (!TipoDocumento.values(TipoPersona.HUMANA).contains(documento.getTipoDocumento())) {
      throw new DominioException("El campo 'documento' por ser Humano, solo puede ser " + TipoDocumento.values(TipoPersona.HUMANA));
    }
    if (direccion == null || direccion.isBlank()) {
      throw new DominioException("El campo 'direccion' es obligatorio, en la persona humana");
    }
    if (contactos == null || contactos.isEmpty()) {
      throw new DominioException("El campo 'contactos' es obligatorio, en la persona humana");
    }
    if (contactos.stream().noneMatch(MedioContacto::getEsPrincipal)) {
      throw new DominioException("Debe tener al menos un contacto principal");
    }
  }

  public Integer getEdad() {
    if (this.fechaNacimiento == null) return null;
    return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
  }

  @Override
  public TipoPersona getTipo() {
    return TipoPersona.HUMANA;
  }

  @Override
  public List<MedioContacto> getContactosPrincipales() {
    return this.contactos.stream().filter(MedioContacto::getEsPrincipal).toList();
  }

  @Override
  public List<MedioContacto> getContactos() {
    return this.contactos;
  }

  @Override
  public String getNombreCompleto() {
    return getNombre() +  " " + getApellido();
  }

  public void actualizarDatos(String nombre, String apellido, Documento documento,
                              LocalDate fechaNacimiento, Genero genero, String direccion,
                              List<MedioContacto> contactos) {
    checkDatos(nombre, documento, direccion, contactos);
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.genero = genero == null ? Genero.SIN_ESPECIFICAR : genero;
    this.direccion = direccion;
    this.contactos = new ArrayList<>(contactos);
  }

}