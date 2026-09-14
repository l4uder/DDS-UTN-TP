package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "representantes")
public class Representante {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "nombre")
  private String nombre;
  @Column(name = "apellido")
  private String apellido;
  @Embedded
  private Documento documento;
  @Column(name = "direccion")
  private String direccion;
  @Transient
  private List<MedioContacto> contactos;

  public Representante(String nombre, String apellido,
                       Documento documento, String direccion,
                       List<MedioContacto> contactos) {
    chekDatos(nombre, contactos);
    this.nombre = nombre;
    this.apellido = apellido;
    this.documento = documento;
    this.direccion = direccion;
    this.contactos = new ArrayList<>(contactos);
  }

  private void chekDatos(String nombre, List<MedioContacto> contactos) {
    if (nombre == null || nombre.isBlank()) throw new DominioException("El campo nombre es obligatorio, en representante");
    if (contactos == null || contactos.isEmpty()) {
      throw new DominioException("Debe proporcionar al menos un contacto, en representante");
    }
    if (contactos.stream().noneMatch(MedioContacto::getEsPrincipal)) {
      throw new DominioException("Debe tener al menos un contacto principal, el representante");
    }
  }

  public List<MedioContacto> getContactosPrincipales() {
    return this.contactos.stream().filter(MedioContacto::getEsPrincipal).toList();
  }

}
