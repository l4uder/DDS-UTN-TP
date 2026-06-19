package ar.edu.utn.frba.dds.donatrack.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.MedioContacto;
import java.util.ArrayList;
import java.util.List;

public abstract class Donante {
  protected List<MedioContacto> contactos;
  protected List<RegistroEntrega> entregas = new ArrayList<>();

  public Donante(List<MedioContacto> contactos) {
    if (contactos == null || contactos.isEmpty()) {
      throw new DomainValidationException("La lista de contactos no puede estar vacía ni ser null");
    }

    this.contactos = new ArrayList<>(contactos);

    if (this.contactos.stream().noneMatch(MedioContacto::getPrincipal)) {
      throw new DomainValidationException("Debe tener al menos un contacto principal");
    }
  }

  public void agregarContactoPrincipal(MedioContacto contacto) {
    if (contacto == null) {
      throw new DomainValidationException("El medio de contacto principal no puede ser null");
    }

    this.contactos.forEach(c -> c.setPrincipal(false));

    MedioContacto contactoExistente = this.contactos.stream()
        .filter(c -> c.esIgualA(contacto))
        .findFirst()
        .orElse(null);

    if (contactoExistente != null) { // if existe
      contactoExistente.setPrincipal(true);
    } else {
      contacto.setPrincipal(true);
      this.contactos.add(contacto);
    }
  }

  public void agregarContactoSecundario(MedioContacto contacto) {
    if (contacto == null) {
      throw new DomainValidationException("El medio de contacto no puede ser null");
    }

    boolean existe = this.contactos.stream().anyMatch(c -> c.esIgualA(contacto));

    if (!existe) {
      contacto.setPrincipal(false);
      this.contactos.add(contacto);
    }
  }

  public MedioContacto getContactoPrincipal() {
    return this.contactos.stream()
        .filter(MedioContacto::getPrincipal)
        .findFirst()
        .orElseThrow(() -> new DomainValidationException(
            "El donante no posee ningún contacto configurado como principal"));
  }

  public List<MedioContacto> getContactosSecundarios() {
    return this.contactos.stream().filter(c -> !c.getPrincipal()).toList();
  }

  public String getEmail() {
    CorreoDeContato correoBuscado = this.contactos.stream()
        .filter(contacto -> contacto instanceof CorreoDeContato)
        .map(correo -> (CorreoDeContato) correo)
        .findFirst()
        .orElseThrow(() -> new DomainValidationException(
            "El Donante no tiene correo electrónico"));

    return correoBuscado.getCorreo();
  }

}