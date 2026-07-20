package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Donante {
  @Setter
  protected String id;
  @Setter
  protected Documento documento;
  @Setter
  protected List<MedioContacto> contactos;
  protected List<RegistroEntrega> entregas = new ArrayList<>();

  public Donante(Documento documento, List<MedioContacto> contactos) {
    if (contactos == null || contactos.isEmpty()) {
      throw new DomainValidationException("La lista de contactos no puede estar vacía ni ser null");
    }
    this.documento = documento;
    this.contactos = new ArrayList<>(contactos);

    if (this.contactos.stream().noneMatch(MedioContacto::getPrincipal)) {
      throw new DomainValidationException("Debe tener al menos un contacto principal");
    }
  }

  public abstract TipoDonante getTipo();

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
    return buscarEmail()
        .orElseThrow(() -> new DomainValidationException(
            "El Donante no tiene correo electrónico"));
  }

  public Optional<String> buscarEmail() {
    return this.contactos.stream()
        .filter(contacto -> contacto instanceof CorreoDeContato)
        .map(correo -> ((CorreoDeContato) correo).getCorreo())
        .findFirst();
  }

  public void recibirNotificacion(String mensaje) {
    List<MedioContacto> contactos = getContactos();
    contactos.forEach(c -> c.notificar(mensaje));
  }

  public RegistroEntrega getUltimaEntrega() {
    //return this.entregas.stream().max(Comparator.comparing(r -> r.getFecha())).orElse(null);
    if (this.entregas.isEmpty()) {
      return null;
    }
    return this.entregas.get(this.entregas.size() - 1);
  }

  public boolean estaAusentePorMasDe(Integer dias) {
    RegistroEntrega ultima = this.getUltimaEntrega();
    if (ultima == null) return false; // A los nuevos No los vamos a considerar como ausentes

    LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
    return ultima.getFecha().isBefore(fechaLimite);
  }

  abstract public String getNombreCompleto();

}