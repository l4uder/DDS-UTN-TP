package ar.edu.utn.frba.dds.donatrack.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.MedioContacto;
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
  private MedioContacto medioDeContactoPred;
  private List<MedioContacto> contactos;

  public Representante(String nombre, String apellido, LocalDate fechaNacimiento,
                       Documento documento, Genero genero, String direccion,
                       MedioContacto medioContPred, List<MedioContacto> contactos) {
    if (documento.getTipoDocumento() == TipoDocumento.CUIT) {
      throw new DomainValidationException("Un representante no puede tener un CUIT");
    }
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.documento = documento;
    this.genero = genero;
    this.direccion = direccion;
    this.medioDeContactoPred = medioContPred;
    this.contactos = contactos != null ? new ArrayList<>(contactos) : new ArrayList<>();
  }

  public Integer getEdad() {
    return Period.between(fechaNacimiento, LocalDate.now()).getYears();
  }
}
