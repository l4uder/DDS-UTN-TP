package ar.edu.utn.frba.dds.donatrack.donante;

import ar.edu.utn.frba.dds.donatrack.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.contacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.contacto.MedioContacto;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class PersonaHumana extends Donante {
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
  private Documento documento;
  private Genero genero;
  private String direccion;

  public PersonaHumana(String nombre, String apellido, Documento documento,
                       LocalDate fechaNacimiento, Genero genero, String direccion,
                       MedioContacto medioContPred, List<MedioContacto> contactosSecundarios) {
    super(medioContPred, contactosSecundarios);
      if (documento.getTipoDocumento() == TipoDocumento.CUIT) {
          throw new DomainValidationException("La persona humana no puede tener un CUIT");
      }
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.documento = documento;
    this.genero = genero;
    this.direccion = direccion;
  }

  public Integer getEdad() {
    return Period.between(fechaNacimiento, LocalDate.now()).getYears();
  }

  public Documento getDocumento() {
      return this.documento;
  }

  public String getNombre() {
      return this.nombre;
  }

  public String getApellido() {
      return this.apellido;
  }

    @Override
  public String toString() {
      return "PersonaHumana{" +
                "nombre: " + nombre +
                ", apellido: " + apellido +
                ", documento: " + documento.getTipoDocumento().toString() + documento.getDetalle() +
                '}';
  }
}
