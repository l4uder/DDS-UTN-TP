package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.dominio.medioContacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.Representante;
import ar.edu.utn.frba.dds.donatrack.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.dominio.medioContacto.MedioContacto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepresentanteBuilder {
  private String nombre = "Daniela";
  private String apellido = "Vega";
  private LocalDate fechaNacimiento = LocalDate.of(2004, 5, 12);
  private Documento documento = new Documento(TipoDocumento.DNI, "46499829");
  private Genero genero = Genero.FEMENINO;
  private String direccion = "Av. San Martín 100";
  private MedioContacto medioContPred = new CorreoDeContato("daniela@srl.com");
  private List<MedioContacto> contactos = new ArrayList<>();

  public RepresentanteBuilder conNombre(String nombre) {
    this.nombre = nombre;
    return this;
  }

  public RepresentanteBuilder conApellido(String apellido) {
    this.apellido = apellido;
    return this;
  }

  public RepresentanteBuilder conFechaNacimiento(LocalDate fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
    return this;
  }

  public RepresentanteBuilder conDocumento(Documento documento) {
    this.documento = documento;
    return this;
  }

  public RepresentanteBuilder conGenero(Genero genero) {
    this.genero = genero;
    return this;
  }

  public RepresentanteBuilder conDireccion(String direccion) {
    this.direccion = direccion;
    return this;
  }

  public RepresentanteBuilder conMedioContactoPredeterminado(MedioContacto medioContacto) {
    this.medioContPred = medioContacto;
    return this;
  }

  public RepresentanteBuilder conMedioContactosSecundarios(List<MedioContacto> contactos) {
    this.contactos = contactos;
    return this;
  }

  public Representante build() {
    return new Representante(nombre, apellido, fechaNacimiento, documento, genero, direccion, medioContPred, contactos);
  }
}