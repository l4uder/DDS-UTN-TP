package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Representante {
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
  private Documento documento;
  private Genero genero;
  private String direccion;
  private MedioContacto contacto;

  public Representante(String nombre, String apellido, LocalDate fechaNacimiento,
                       Documento documento, Genero genero, String direccion,
                       MedioContacto contacto) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.documento = documento;
    this.genero = genero;
    this.direccion = direccion;
    this.contacto = contacto;
  }

  public Integer getEdad() {
    return Period.between(fechaNacimiento, LocalDate.now()).getYears();
  }
}
