package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
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

  public PersonaHumanaBuilder conAgregarContacto(MedioContacto contacto) {
    this.contactos.add(contacto);
    return this;
  }

  public PersonaHumanaBuilder vaciarContactos() {
    this.contactos = new ArrayList<>();
    return this;
  }

  public Donante build() {
    return Donante.personaHumana(nombre, apellido, documento, fechaNacimiento,
        genero, direccion,  contactos);
  }
}
