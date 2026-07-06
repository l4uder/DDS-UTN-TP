package ar.edu.utn.frba.dds.donatrack.donaciones.dto.comun;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.SmsDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.WhatsappDeContato;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import java.util.List;

public class ContactoMapper {

  private ContactoMapper() {
  }

  public static List<MedioContacto> aDominio(List<ContactoDto> contactos) {
    if (contactos == null) {
      return List.of();
    }
    return contactos.stream().map(ContactoMapper::aContacto).toList();
  }

  public static MedioContacto aContacto(ContactoDto dto) {
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

  public static ContactoDto aDto(MedioContacto contacto) {
    if (contacto instanceof CorreoDeContato correo) {
      return new ContactoDto("EMAIL", correo.getCorreo(), correo.getPrincipal());
    }
    if (contacto instanceof SmsDeContato sms) {
      return new ContactoDto("SMS", sms.getTelefono(), sms.getPrincipal());
    }
    WhatsappDeContato whatsapp = (WhatsappDeContato) contacto;
    return new ContactoDto("WHATSAPP", whatsapp.getTelefono(), whatsapp.getPrincipal());
  }

}
