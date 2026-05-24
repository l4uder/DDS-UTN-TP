package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.contacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.donante.Representante;
import ar.edu.utn.frba.dds.donatrack.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.contacto.MedioContacto;
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

  public RepresentanteBuilder conDocumento(TipoDocumento tipo, String numero) {
    this.documento = new Documento(tipo, numero);
    return this;
  }

  public Representante build() {
    return new Representante(nombre, apellido, fechaNacimiento, documento, genero, direccion, medioContPred, contactos);
  }
}