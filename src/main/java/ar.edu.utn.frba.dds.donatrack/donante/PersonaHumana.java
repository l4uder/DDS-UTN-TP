package ar.edu.utn.frba.dds.donatrack.donante;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class PersonaHumana extends Donante{
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
  private Documento documento;
  private Genero genero;
  private String direccion;

  public PersonaHumana(String nombre, String apellido,
                       Documento documento,
                       LocalDate fechaNacimiento, Genero genero,
                       String direccion, MedioContacto medioContPred, List<MedioContacto> contactosSecundarios) {
    super(medioContPred, contactosSecundarios);
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
}
