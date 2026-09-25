package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.util.ArrayList;
import java.util.List;

public class RepresentanteBuilder {
  private String nombre;
  private String apellido;
  private Documento documento;
  private String direccion;
  private List<MedioContacto> contactos;

  public RepresentanteBuilder() {
    this.contactos = new ArrayList<>();
  }

  public RepresentanteBuilder conNombre(String nombre) {
    this.nombre = nombre;
    return this;
  }

  public RepresentanteBuilder conApellido(String apellido) {
    this.apellido = apellido;
    return this;
  }

  public RepresentanteBuilder conDocumento(Documento documento) {
    this.documento = documento;
    return this;
  }

  public RepresentanteBuilder conDireccion(String direccion) {
    this.direccion = direccion;
    return this;
  }

  public RepresentanteBuilder conAgregarContacto(MedioContacto contacto) {
    this.contactos.add(contacto);
    return this;
  }

  public Representante build() {
    return new Representante(nombre, apellido, documento, direccion, contactos);
  }
}