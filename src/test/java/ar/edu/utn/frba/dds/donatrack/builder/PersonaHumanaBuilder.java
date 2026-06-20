package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.MedioContacto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonaHumanaBuilder {
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
  private Documento documento;
  private Genero genero;
  private String direccion;
  private List<MedioContacto> contactos;

  public PersonaHumanaBuilder() {
    this.contactos = new ArrayList<>();
  }

  public PersonaHumanaBuilder conNombre(String nombre) {
    this.nombre = nombre;
    return this;
  }

  public PersonaHumanaBuilder conApellido(String apellido) {
    this.apellido = apellido;
    return this;
  }

  public PersonaHumanaBuilder conFechaNacimiento(LocalDate fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
    return this;
  }

  public PersonaHumanaBuilder conDocumento(Documento documento) {
    this.documento = documento;
    return this;
  }

  public PersonaHumanaBuilder conGenero(Genero genero) {
    this.genero = genero;
    return this;
  }

  public PersonaHumanaBuilder conDireccion(String direccion) {
    this.direccion = direccion;
    return this;
  }

  public PersonaHumanaBuilder conContactoPrincipal(MedioContacto contacto) {
    contacto.setPrincipal(true);
    this.contactos.add(contacto);
    return this;
  }

  public PersonaHumanaBuilder conContactoSecundario(MedioContacto contacto) {
    this.contactos.add(contacto);
    return this;
  }

  public PersonaHumanaBuilder conContactosSecundarios(List<MedioContacto> contactosSecundarios) {
    if (contactosSecundarios != null) {
      contactosSecundarios.forEach(c -> c.setPrincipal(false));
      this.contactos.addAll(contactosSecundarios);
    }
    return this;
  }

  public PersonaHumanaBuilder vaciarContactos() {
    this.contactos = new ArrayList<>();
    return this;
  }

  public PersonaHumana build() {
    return new PersonaHumana(nombre, apellido, documento, fechaNacimiento,
        genero, direccion, contactos);
  }
}
