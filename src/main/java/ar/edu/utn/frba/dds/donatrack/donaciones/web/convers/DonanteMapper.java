package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDonante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.contacto.ContactoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.documento.DocumentoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteResumenResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import java.util.ArrayList;
import java.util.List;

public class DonanteMapper {

  private DonanteMapper() {}

  public static Donante aDominio(DonanteRequest request) {
    Documento documento = DocumentoMapper.aDominio(request.documento());
    List<MedioContacto> contactos = ContactoMapper.aDominio(request.contactos());
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

  public static DonanteResponse aDto(Donante donante) {
    DocumentoDto documentoDto = DocumentoMapper.aDto(donante.getDocumento());
    List<ContactoDto> contactosDto = ContactoMapper.aDto(donante.getContactos());

    if (donante instanceof PersonaHumana humana) {
      return new DonanteResponse(
          humana.getId(),
          "HUMANA",
          documentoDto,
          contactosDto,
          humana.getNombre(),
          humana.getApellido(),
          humana.getFechaNacimiento(),
          humana.getGenero() == null ? null : humana.getGenero().name(),
          humana.getDireccion(),
          null, null, null);
    }

    if (donante instanceof PersonaJuridica juridica) {
      return new DonanteResponse(
          juridica.getId(),
          "JURIDICA",
          documentoDto,
          contactosDto,
          null, null, null, null, null,
          juridica.getRazonSocial(),
          juridica.getTipoOrganizacion() == null ? null : juridica.getTipoOrganizacion().name(),
          juridica.getRubro());
    }

    throw new IllegalArgumentException("Tipo de donante no soportado: " + donante.getClass().getName());
  }

  public static DonanteResumenResponse aDtoResumen(Donante donante) {
    return new DonanteResumenResponse(
        donante.getId(),
        donante.getTipo().name(),
        donante.getNombreCompleto());
  }

  public static void actualizar(Donante donante, DonanteRequest request) {
    if (request.tipo() != null) {
      //TipoDonante nuevoTipo = parseEnum(TipoDonante.class, request.tipo(), "tipo de donante");
      //if (donante.getTipo() != nuevoTipo) throw new DomainValidationException("No se puede modificar el tipo de un donante existente.");
      throw new DomainValidationException("No puede modificar el tipo de un donante");
    }

    if (request.documento() != null) donante.setDocumento(DocumentoMapper.aDominio(request.documento()));
    if (request.contactos() != null)  donante.setContactos(ContactoMapper.aDominio(request.contactos()));

    if (donante instanceof PersonaHumana humana) {
      if (request.nombre() != null) humana.setNombre(request.nombre());
      if (request.apellido() != null) humana.setApellido(request.apellido());
      if (request.fechaNacimiento() != null) humana.setFechaNacimiento(request.fechaNacimiento());
      if (request.direccion() != null) humana.setDireccion(request.direccion());
      if (request.genero() != null) humana.setGenero(parseEnum(Genero.class, request.genero(), "genero"));
    }

    if (donante instanceof PersonaJuridica juridica) {
      if (request.razonSocial() != null) juridica.setRazonSocial(request.razonSocial());
      if (request.rubro() != null) juridica.setRubro(request.rubro());
      if (request.tipoOrganizacion() != null) juridica.setTipoOrganizacion(parseEnum(TipoOrganizacion.class, request.tipoOrganizacion(), "tipo de organizacion"));
    }
  }

  //================== FUNCIONES AUXILIARES ================
  private static <E extends Enum<E>> E parseEnum(Class<E> tipo, String valor, String campo) {
    try {
      return Enum.valueOf(tipo, valor.toUpperCase());
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new DomainValidationException("Valor invalido para " + campo + ": " + valor);
    }
  }

}