package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
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
    checkDatos(nombre, documento, direccion);
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.genero = genero == null ? Genero.SIN_ESPECIFICAR : genero;
    this.direccion = direccion;
  }

  private void checkDatos(String nombre, Documento documento, String direccion) {
    if (nombre == null || nombre.isBlank()) {
      throw new DomainValidationException("El campo 'nombre' es obligatorio");
    }
    TipoDocumento tipo = documento.getTipoDocumento();
    if (!(tipo == TipoDocumento.DNI || tipo == TipoDocumento.PASAPORTE)) {
      throw new DomainValidationException("El campo 'documento' por ser Humano, solo puede ser DNI o PASAPORTE");
    }
    if (direccion == null || direccion.isBlank()) {
      throw new DomainValidationException("El campo 'direccion' es obligatorio");
    }
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

  public void actualizarDatos(String nombre, String apellido, Documento documento,
                              LocalDate fechaNacimiento, Genero genero, String direccion,
                              List<MedioContacto> contactos) {
    super.actualizarDatosBase(documento, contactos);
    checkDatos(nombre, documento, direccion);
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.genero = genero == null ? Genero.SIN_ESPECIFICAR : genero;
    this.direccion = direccion;
  }

  @Override
  public String toString() {
    return "PersonaHumana{"
        + "nombre completo: " + nombre + " " + apellido + " "
        + "documento: " + documento.getTipoDocumento().toString() + " " + documento.getDetalle()
        + '}';
  }

}