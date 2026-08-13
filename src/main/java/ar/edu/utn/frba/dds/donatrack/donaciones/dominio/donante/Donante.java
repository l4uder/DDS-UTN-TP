package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.entrega.RegistroEntrega;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.TipoDonante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.persona.Humana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Juridica;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Donante {
  @Setter
  private String id;
  private Documento documento;
  private List<RegistroEntrega> entregas;
  private final TipoDonante tipoDonante;

  private Donante(Documento documento, TipoDonante tipoDonante) {
    checkDatos(documento);
    this.documento = documento;
    this.entregas = new ArrayList<>();
    this.tipoDonante = tipoDonante;
  }

  private void checkDatos(Documento documento) {
    if (documento == null) {
      throw new DominioException("El documento no puede ser null");
    }
  }

  public MedioContacto getPrimerContactoPrincipal() {
    return getContactosPrincipales().stream()
        .findFirst()
        .orElseThrow(() -> new DominioException( "El donante no posee ningún contacto configurado como principal"));
  }

/*
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
*/
  public void recibirNotificacion(String mensaje) {
    List<MedioContacto> contactos = getContactosPrincipales();
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

  public static Donante personaHumana(String nombre, String apellido, Documento documento,
                                      LocalDate fechaNacimiento, Genero genero, String direccion,
                                      List<MedioContacto> contactos) {
    TipoDonante tipoPersona = new Humana(nombre, apellido, documento, fechaNacimiento, genero, direccion, contactos);
    return new Donante(documento, tipoPersona);
  }

  public static Donante personaJuridica(String razonSocial, Documento documento, TipoOrganizacion tipo,
                                        String rubro, List<Representante> representantes) {
    TipoDonante tipoPersona = new Juridica(razonSocial, tipo, rubro, documento, representantes);
    return new Donante(documento, tipoPersona);
  }

  public List<MedioContacto> getContactosPrincipales() {
    return this.tipoDonante.getContactosPrincipales();
  }

  public List<MedioContacto> getContactosSecundarios() {
    return this.tipoDonante.getContactosSecundarios();
  }

  public String getNombreCompleto() {
    return tipoDonante.getNombreCompleto();
  }

  public TipoPersona getTipoPersona() {
    return tipoDonante.getTipo();
  }

  private void actualizarDatosBase(Documento documento) {
    checkDatos(documento);
    this.documento = documento;
  }

  public void actualizarDatosHumana(String nombre, String apellido, Documento documento,
                                    LocalDate fechaNacimiento, Genero genero, String direccion,
                                    List<MedioContacto> contactos) {
    if (!(this.tipoDonante instanceof Humana)) throw new DominioException("No se pueden actualizar los datos de una persona humana con datos de una persona jurídica");

    this.actualizarDatosBase(documento);
    ((Humana) this.tipoDonante).actualizarDatos(nombre, apellido, documento, fechaNacimiento, genero, direccion, contactos);
  }

  public void actualizarDatosJuridica(String razonSocial, Documento documento, TipoOrganizacion tipo,
                                      String rubro, List<Representante> representantes) {
    if (!(this.tipoDonante instanceof Juridica)) throw new DominioException("No se pueden actualizar los datos de una persona jurídica con datos de una persona humana");

    this.actualizarDatosBase(documento);
    ((Juridica) this.tipoDonante).actualizarDatos(razonSocial, tipo, rubro, documento, representantes);
  }

}