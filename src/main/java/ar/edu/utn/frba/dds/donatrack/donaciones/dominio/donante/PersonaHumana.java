package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonaHumana extends Donante {
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
  private Genero genero;
  private String direccion;

  public PersonaHumana(String nombre, String apellido, Documento documento,
                       LocalDate fechaNacimiento, Genero genero, String direccion,
                       List<MedioContacto> contactos) {
    super(documento, contactos);
    if (documento.getTipoDocumento() == TipoDocumento.CUIT) {
      throw new DomainValidationException("La persona humana no puede tener un CUIT");
    }
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.genero = genero;
    this.direccion = direccion;
  }

  @Override
  public TipoDonante getTipo() {
    return TipoDonante.HUMANA;
  }

  @Override
  public String getNombreCompleto() {
    return getNombre() +  " " + getApellido();
  }

  public Integer getEdad() {
    return Period.between(fechaNacimiento, LocalDate.now()).getYears();
  }

  @Override
  public String toString() {
    return "PersonaHumana{"
        + "nombre: " + nombre
        + ", apellido: " + apellido
        + ", documento: " + documento.getTipoDocumento().toString()
        + documento.getDetalle()
        + '}';
  }
}