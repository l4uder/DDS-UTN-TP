package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.contacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.contacto.MedioContacto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonaHumanaBuilder {
  private String nombre = "Nombre Default";
  private String apellido = "Apellido Default";
  private LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
  private Documento documento = new Documento(TipoDocumento.DNI, "12345678");
  private Genero genero = Genero.X;
  private String direccion = "Direccion Default";
  private MedioContacto medioContPred = new CorreoDeContato("default@mail.com");
  private List<MedioContacto> contactosSecundarios = new ArrayList<>();

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

  public PersonaHumanaBuilder conDocumento(TipoDocumento tipo, String numero) {
    this.documento = new Documento(tipo, numero);
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

  public PersonaHumanaBuilder conEmail(String email) {
    this.medioContPred = new CorreoDeContato(email);
    return this;
  }

  public PersonaHumanaBuilder conContactosSecundarios(List<MedioContacto> contactos) {
    this.contactosSecundarios = contactos;
    return this;
  }

  public PersonaHumanaBuilder vaciarContactos() {
    this.medioContPred = null;
    this.contactosSecundarios = new ArrayList<>();
    return this;
  }

  public PersonaHumana build() {
    return new PersonaHumana(nombre, apellido, documento, fechaNacimiento,
        genero, direccion, medioContPred, contactosSecundarios);
  }
}
