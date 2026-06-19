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
  private String nombre = "Nombre Default";
  private String apellido = "Apellido Default";
  private LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
  private Documento documento = new Documento(TipoDocumento.DNI, "12345678");
  private Genero genero = Genero.X;
  private String direccion = "Direccion Default";
  private List<MedioContacto> contactos = new ArrayList<>();

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
    CorreoDeContato correo = new CorreoDeContato(email);
    correo.setPrincipal(true);
    this.contactos.add(correo);
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
