package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.SmsDeContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.WhatsappDeContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.contacto.ContactoDto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactoMapper {

  public static MedioContacto aDominio(ContactoDto contactoDto) {
    if (contactoDto.medio() == null) {
      throw new DominioException("Cada contacto necesita un 'medio', valores posibles: [EMAIL, SMS, WHATSAPP] ");
    }
    return switch (contactoDto.medio().toUpperCase()) {
      case "EMAIL" -> new CorreoDeContacto(
          contactoDto.valor(),
          Boolean.TRUE.equals(contactoDto.principal())
      );
      case "SMS" -> new SmsDeContacto(
          contactoDto.valor(),
          Boolean.TRUE.equals(contactoDto.principal())
      );
      case "WHATSAPP" -> new WhatsappDeContacto(
          contactoDto.valor(),
          Boolean.TRUE.equals(contactoDto.principal())
      );
      default -> throw new DominioException(
          "EL medio de contacto: " + contactoDto.medio() + " no existe debe ser: [EMAIL, SMS o WHATSAPP] ");
    };
  }

  public static ContactoDto aDto(MedioContacto contacto) {
    if (contacto instanceof CorreoDeContacto correo) {
      return new ContactoDto("EMAIL", correo.getDetalle(), correo.getEsPrincipal());
    }
    if (contacto instanceof SmsDeContacto sms) {
      return new ContactoDto("SMS", sms.getDetalle(), sms.getEsPrincipal());
    }
    WhatsappDeContacto whatsapp = (WhatsappDeContacto) contacto;
    return new ContactoDto("WHATSAPP", whatsapp.getDetalle(), whatsapp.getEsPrincipal());
  }

  //====================  FUNCIONES AUXILIARES =====================
  public static List<MedioContacto> aDominio(List<ContactoDto> contactosDto) {
    if (contactosDto == null || contactosDto.isEmpty()) {
      throw new DominioException("La lista de 'contactos' no puede estar vacía ni ser null");
    }
    return contactosDto.stream().map(ContactoMapper::aDominio).toList();
  }

  public static List<ContactoDto> aDto(List<MedioContacto> contactos) {
    return contactos.stream().map(ContactoMapper::aDto).toList();
  }

}
