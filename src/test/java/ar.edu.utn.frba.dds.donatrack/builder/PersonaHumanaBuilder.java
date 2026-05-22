package ar.edu.utn.frba.dds.donatrack.builder;

import ar.edu.utn.frba.dds.donatrack.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.share.TipoContacto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonaHumanaBuilder {
  private String nombre = "Juan";
  private String apellido = "Villa";
  private Documento documento = new Documento(TipoDocumento.DNI, "45123321");
  private LocalDate fechaNacimiento = LocalDate.of(2000, 5, 10);
  private Genero genero = Genero.MASCULINO;
  private String direccion = "Av. Corrientes 1855";
  private MedioContacto medioContPred = new MedioContacto(TipoContacto.CORREO, "juanitocabj@gmail.com");
  private List<MedioContacto> contactosSecundarios = new ArrayList<>();

  public PersonaHumanaBuilder conNombre(String nombre) {
    this.nombre = nombre;
    return this;
  }

  public PersonaHumanaBuilder conContactoPredeterminado(MedioContacto medioContPred) {
    this.medioContPred = medioContPred;
    return this;
  }

  public PersonaHumanaBuilder vaciarContactos() {
    this.medioContPred = null;
    this.contactosSecundarios = new ArrayList<>();
    return this;
  }

  public PersonaHumana build() {
    return new PersonaHumana(nombre, apellido, documento, fechaNacimiento, genero, direccion, medioContPred, contactosSecundarios);
  }
}