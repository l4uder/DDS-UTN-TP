package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Representante {
  private String nombre;
  private String apellido;
  private Documento documento;
  private String direccion;
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
