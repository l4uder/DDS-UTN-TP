package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.persona;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "donantes_humanas")
@PrimaryKeyJoinColumn(name = "id_donante")
public class Humana extends Donante {
  @Column(name = "nombre")
  private String nombre;
  @Column(name = "apellido")
  private String apellido;
  @Column(name = "fecha_nacimiento")
  private LocalDate fechaNacimiento;
  @Column(name = "genero")
  @Enumerated(EnumType.STRING)
  private Genero genero;
  @Column(name = "direccion")
  private String direccion;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_donante")
  private List<MedioContacto> contactos;

  public Humana(String nombre, String apellido, Documento documento,
                LocalDate fechaNacimiento, Genero genero, String direccion,
                List<MedioContacto> contactos) {
    super(documento, "HUMANA");
    checkDatos(nombre, documento, direccion, contactos);
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.genero = genero == null ? Genero.SIN_ESPECIFICAR : genero;
    this.direccion = direccion;
    this.contactos = new ArrayList<>(contactos);
  }

  private void checkDatos(String nombre, Documento documento, String direccion, List<MedioContacto> contactos) {
    if (nombre == null || nombre.isBlank()) {
      throw new DominioException("El campo 'nombre' es obligatorio, en la persona humana");
    }
    if (documento == null) {
      throw new DominioException("El documento es obligatorio en la persona humana, opciones posibles: " + TipoDocumento.valoresPosiblesHumana());
    }
    if (!TipoDocumento.valoresPosiblesHumana().contains(documento.getTipoDocumento())) {
      throw new DominioException("El campo 'documento' por ser Humano, solo puede ser " + TipoDocumento.valoresPosiblesHumana());
    }
    if (direccion == null || direccion.isBlank()) {
      throw new DominioException("El campo 'direccion' es obligatorio, en la persona humana");
    }
    if (contactos == null || contactos.isEmpty()) {
      throw new DominioException("El campo 'contactos' es obligatorio, en la persona humana");
    }
    if (contactos.stream().noneMatch(MedioContacto::getEsPrincipal)) {
      throw new DominioException("Debe tener al menos un contacto principal");
    }
  }

  public Integer getEdad() {
    if (this.fechaNacimiento == null) return null;
    return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
  }

  @Override
  public String getNombreCompleto() {
    return getNombre() +  " " + getApellido();
  }

  @Override
  public String getTipo() {
    return "HUMANA";
  }

  @Override
  public List<MedioContacto> getContactos() {
    return this.contactos;
  }

  @Override
  public List<MedioContacto> getContactosPrincipales() {
    return this.contactos.stream().filter(MedioContacto::getEsPrincipal).toList();
  }

  public void actualizarDatos(String nombre, String apellido, Documento documento,
                              LocalDate fechaNacimiento, Genero genero, String direccion,
                              List<MedioContacto> contactos) {
    super.actualizarDatosBase(documento);
    checkDatos(nombre, documento, direccion, contactos);
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.genero = genero == null ? Genero.SIN_ESPECIFICAR : genero;
    this.direccion = direccion;
    this.contactos = new ArrayList<>(contactos);
  }

}