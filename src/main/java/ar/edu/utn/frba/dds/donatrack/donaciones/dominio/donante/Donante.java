package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Donante {
  @Setter
  protected String id;
  protected Documento documento;
  protected List<MedioContacto> contactos;
  protected List<RegistroEntrega> entregas = new ArrayList<>();

  public Donante(Documento documento, List<MedioContacto> contactos) {
    checkDatosBase(documento, contactos);
    this.documento = documento;
    this.contactos = new ArrayList<>(contactos);
  }

  private void checkDatosBase(Documento documento, List<MedioContacto> contactos) {
    if (documento == null) {
      throw new DomainValidationException("El documento es obligatorio");
    }
    if (contactos == null || contactos.isEmpty()) {
      throw new DomainValidationException("Debe proporcionar al menos un contacto");
    }
    if (contactos.stream().noneMatch(MedioContacto::getEsPrincipal)) {
      throw new DomainValidationException("Debe tener al menos un contacto principal");
    }
  }

  public MedioContacto getPrimerContactoPrincipal() {
    return getContactosPrincipales().stream()
        .findFirst()
        .orElseThrow(() -> new DomainValidationException( "El donante no posee ningún contacto configurado como principal"));
  }

  public List<MedioContacto> getContactosPrincipales() {
    return this.contactos.stream().filter(c -> c.getEsPrincipal()).toList();
  }

  public List<MedioContacto> getContactosSecundarios() {
    return this.contactos.stream().filter(c -> !c.getEsPrincipal()).toList();
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
    if (this.entregas.isEmpty()) return null;

    return this.entregas.get(this.entregas.size() - 1);
  }

  public boolean estaAusentePorMasDe(Integer dias) {
    RegistroEntrega ultima = this.getUltimaEntrega();
    if (ultima == null) return false; // A los nuevos No los vamos a considerar como ausentes

    LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
    return ultima.getFecha().isBefore(fechaLimite);
  }

  abstract public String getNombreCompleto();

  public abstract TipoDonante getTipo();

  protected void actualizarDatosBase(Documento documento, List<MedioContacto> contactos) {
    checkDatosBase(documento, contactos);
    this.documento = documento;
    this.contactos = new ArrayList<>(contactos);
  }

}