package ar.edu.utn.frba.dds.donatrack.donaciones.dto;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.SmsDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.WhatsappDeContato;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import java.util.ArrayList;
import java.util.List;

public class DonanteMapper {

  private DonanteMapper() {
  }

  public static Donante aDominio(DonanteRequest request) {
    if (request.tipo() == null) {
      throw new DomainValidationException("El campo 'tipo' es obligatorio (HUMANA o JURIDICA)");
    }
    if (request.documento() == null) {
      throw new DomainValidationException("El campo 'documento' es obligatorio");
    }

    Documento documento = new Documento(
        parseEnum(TipoDocumento.class, request.documento().tipo(), "tipo de documento"),
        request.documento().numero());
    List<MedioContacto> contactos = aContactos(request.contactos());

    return switch (request.tipo().toUpperCase()) {
      case "HUMANA" -> new PersonaHumana(
          request.nombre(),
          request.apellido(),
          documento,
          request.fechaNacimiento(),
          request.genero() == null ? null : parseEnum(Genero.class, request.genero(), "genero"),
          request.direccion(),
          contactos);
      case "JURIDICA" -> new PersonaJuridica(
          request.razonSocial(),
          request.tipoOrganizacion() == null
              ? TipoOrganizacion.SIN_ESPECIFICAR
              : parseEnum(TipoOrganizacion.class, request.tipoOrganizacion(),
                  "tipo de organizacion"),
          request.rubro(),
          documento,
          null,
          contactos);
      default -> throw new DomainValidationException(
          "Tipo de donante invalido: " + request.tipo() + " (HUMANA o JURIDICA)");
    };
  }

  public static DonanteResponse aResponse(Donante donante) {
    DocumentoDto documento = new DocumentoDto(
        donante.getDocumento().getTipoDocumento().name(),
        donante.getDocumento().getDetalle());
    List<ContactoDto> contactos = aContactosDto(donante);

    if (donante instanceof PersonaHumana humana) {
      return new DonanteResponse(
          humana.getId(), "HUMANA", documento, contactos,
          humana.getNombre(), humana.getApellido(), humana.getFechaNacimiento(),
          humana.getGenero() == null ? null : humana.getGenero().name(),
          humana.getDireccion(),
          null, null, null);
    }

    PersonaJuridica juridica = (PersonaJuridica) donante;
    return new DonanteResponse(
        juridica.getId(), "JURIDICA", documento, contactos,
        null, null, null, null, null,
        juridica.getRazonSocial(),
        juridica.getTipoOrganizacion() == null ? null : juridica.getTipoOrganizacion().name(),
        juridica.getRubro());
  }

  private static List<MedioContacto> aContactos(List<ContactoDto> contactos) {
    if (contactos == null || contactos.isEmpty()) {
      throw new DomainValidationException("La lista de contactos no puede estar vacía ni ser null");
    }
    return contactos.stream().map(DonanteMapper::aContacto).toList();
  }

  private static MedioContacto aContacto(ContactoDto dto) {
    if (dto.medio() == null || dto.valor() == null) {
      throw new DomainValidationException("Cada contacto necesita 'medio' y 'valor'");
    }
    MedioContacto contacto = switch (dto.medio().toUpperCase()) {
      case "EMAIL" -> new CorreoDeContato(dto.valor());
      case "SMS" -> new SmsDeContato(dto.valor());
      case "WHATSAPP" -> new WhatsappDeContato(dto.valor());
      default -> throw new DomainValidationException(
          "Medio de contacto invalido: " + dto.medio() + " (EMAIL, SMS o WHATSAPP)");
    };
    contacto.setPrincipal(Boolean.TRUE.equals(dto.principal()));
    return contacto;
  }

  private static List<ContactoDto> aContactosDto(Donante donante) {
    List<ContactoDto> contactos = new ArrayList<>();
    contactos.add(aContactoDto(donante.getContactoPrincipal()));
    donante.getContactosSecundarios().forEach(c -> contactos.add(aContactoDto(c)));
    return contactos;
  }

  private static ContactoDto aContactoDto(MedioContacto contacto) {
    if (contacto instanceof CorreoDeContato correo) {
      return new ContactoDto("EMAIL", correo.getCorreo(), correo.getPrincipal());
    }
    if (contacto instanceof SmsDeContato sms) {
      return new ContactoDto("SMS", sms.getTelefono(), sms.getPrincipal());
    }
    WhatsappDeContato whatsapp = (WhatsappDeContato) contacto;
    return new ContactoDto("WHATSAPP", whatsapp.getTelefono(), whatsapp.getPrincipal());
  }

  private static <E extends Enum<E>> E parseEnum(Class<E> tipo, String valor, String campo) {
    try {
      return Enum.valueOf(tipo, valor.toUpperCase());
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new DomainValidationException("Valor invalido para " + campo + ": " + valor);
    }
  }

}