package ar.edu.utn.frba.dds.donatrack.donaciones.dto.donante;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDonante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.comun.ContactoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.dto.comun.ContactoMapper;
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
    TipoDonante tipo = parseEnum(TipoDonante.class, request.tipo(), "tipo de donante");

    return switch (tipo) {
      case HUMANA -> new PersonaHumana(
          request.nombre(),
          request.apellido(),
          documento,
          request.fechaNacimiento(),
          request.genero() == null ? null : parseEnum(Genero.class, request.genero(), "genero"),
          request.direccion(),
          contactos);
      case JURIDICA -> new PersonaJuridica(
          request.razonSocial(),
          request.tipoOrganizacion() == null
              ? TipoOrganizacion.SIN_ESPECIFICAR
              : parseEnum(TipoOrganizacion.class, request.tipoOrganizacion(),
                  "tipo de organizacion"),
          request.rubro(),
          documento,
          null,
          contactos);
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
    return ContactoMapper.aDominio(contactos);
  }

  private static List<ContactoDto> aContactosDto(Donante donante) {
    List<ContactoDto> contactos = new ArrayList<>();
    contactos.add(ContactoMapper.aDto(donante.getContactoPrincipal()));
    donante.getContactosSecundarios().forEach(c -> contactos.add(ContactoMapper.aDto(c)));
    return contactos;
  }

  private static <E extends Enum<E>> E parseEnum(Class<E> tipo, String valor, String campo) {
    try {
      return Enum.valueOf(tipo, valor.toUpperCase());
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new DomainValidationException("Valor invalido para " + campo + ": " + valor);
    }
  }

}