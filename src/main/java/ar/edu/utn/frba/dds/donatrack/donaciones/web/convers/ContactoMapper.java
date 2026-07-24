package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.SmsDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.WhatsappDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.contacto.ContactoDto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.correo.FabricaClienteCorreoReal;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.sms.FabricaClienteSmsReal;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion.whatsapp.FabricaClienteWhatsappReal;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactoMapper {

  public static MedioContacto aDominio(ContactoDto contactoDto) {
    if (contactoDto.medio() == null || contactoDto.valor() == null) {
      throw new DomainValidationException("Cada contacto necesita 'medio' y 'valor'");
    }
    MedioContacto contacto = switch (contactoDto.medio().toUpperCase()) {
      case "EMAIL" -> new CorreoDeContato(
          contactoDto.valor(), 
          Boolean.TRUE.equals(contactoDto.principal()), 
          FabricaClienteCorreoReal.start()
      );
      case "SMS" -> new SmsDeContato(
          contactoDto.valor(), 
          Boolean.TRUE.equals(contactoDto.principal()), 
          FabricaClienteSmsReal.start()
      );
      case "WHATSAPP" -> new WhatsappDeContato(
          contactoDto.valor(), 
          Boolean.TRUE.equals(contactoDto.principal()), 
          FabricaClienteWhatsappReal.start() 
      );
      default -> throw new DomainValidationException(
          "EL medio de contacto: " + contactoDto.medio() + " no existe debe ser: [EMAIL, SMS o WHATSAPP] ");
    };
    return contacto;
  }

  public static ContactoDto aDto(MedioContacto contacto) {
    if (contacto instanceof CorreoDeContato correo) {
      return new ContactoDto("EMAIL", correo.getCorreo(), correo.getEsPrincipal());
    }
    if (contacto instanceof SmsDeContato sms) {
      return new ContactoDto("SMS", sms.getTelefono(), sms.getEsPrincipal());
    }
    WhatsappDeContato whatsapp = (WhatsappDeContato) contacto;
    return new ContactoDto("WHATSAPP", whatsapp.getTelefono(), whatsapp.getEsPrincipal());
  }

  public static List<MedioContacto> aDominio(List<ContactoDto> contactosDto) {
    if (contactosDto == null || contactosDto.isEmpty()) {
      throw new DomainValidationException("La lista de contactos no puede estar vacía ni ser null");
    }
    return contactosDto.stream().map(ContactoMapper::aDominio).toList();
  }

  public static List<ContactoDto> aDto(List<MedioContacto> contactos) {
    return contactos.stream().map(ContactoMapper::aDto).toList();
  }

}
