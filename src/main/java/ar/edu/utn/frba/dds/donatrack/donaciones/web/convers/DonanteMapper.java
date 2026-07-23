package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaHumana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.PersonaJuridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDonante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.contacto.ContactoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.documento.DocumentoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteResumenResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DonanteMapper {

  public static Donante aDominio(DonanteRequest request) {
    Documento documento = DocumentoMapper.aDominio(request.documento());
    List<MedioContacto> contactos = ContactoMapper.aDominio(request.contactos());
    TipoDonante tipo = aTipoDonante(request.tipo());

    return switch (tipo) {
      case HUMANA -> new PersonaHumana(
          request.nombre(),
          request.apellido(),
          documento,
          request.fechaNacimiento(),
          request.genero() == null ? null : aGenero(request.genero()),
          request.direccion(),
          contactos);
      case JURIDICA -> new PersonaJuridica(
          request.razonSocial(),
          request.tipoOrganizacion() == null ? TipoOrganizacion.SIN_ESPECIFICAR : aTipoOrganizacion(request.tipoOrganizacion()),
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

    throw new DomainValidationException("Tipo de donante no soportado: " + donante.getClass().getName());
  }

  public static DonanteResumenResponse aDtoResumen(Donante donante) {
    return new DonanteResumenResponse(
        donante.getId(),
        donante.getTipo().name(),
        donante.getNombreCompleto());
  }

  public static void actualizarDominio(Donante donante, DonanteRequest request) {
    if (request.tipo() != null) {
      //TipoDonante nuevoTipo = parseEnum(TipoDonante.class, request.tipo(), "tipo de donante");
      //if (donante.getTipo() != nuevoTipo) throw new DomainValidationException("No se puede modificar el tipo de un donante existente.");
      throw new DomainValidationException("No puede modificar el tipo de un donante");
    }

    Documento documentoMerge = request.documento() != null ? DocumentoMapper.aDominio(request.documento()) : donante.getDocumento();
    List<MedioContacto> contactosMerge = request.contactos() != null ? ContactoMapper.aDominio(request.contactos()) : donante.getContactos();

    if (donante instanceof PersonaHumana humana) {
      String nombreMerge = request.nombre() != null ? request.nombre() : humana.getNombre();
      String apellidoMerge = request.apellido() != null ? request.apellido() : humana.getApellido();
      LocalDate nacimientoMerge = request.fechaNacimiento() != null ? request.fechaNacimiento() : humana.getFechaNacimiento();
      String direccionMerge = request.direccion() != null ? request.direccion() : humana.getDireccion();
      Genero generoMerge = request.genero() != null ? aGenero(request.genero()) : humana.getGenero();

      humana.actualizarDatos(nombreMerge, apellidoMerge, documentoMerge, nacimientoMerge, generoMerge, direccionMerge, contactosMerge);
    } else if (donante instanceof PersonaJuridica juridica) {
      String razonSocialMerge = request.razonSocial() != null ? request.razonSocial() : juridica.getRazonSocial();
      String rubroMerge = request.rubro() != null ? request.rubro() : juridica.getRubro();
      TipoOrganizacion tipoOrgMerge = request.tipoOrganizacion() != null ? aTipoOrganizacion(request.tipoOrganizacion()) : juridica.getTipoOrganizacion();
      List<Representante> representantesMerge = juridica.getRepresentantes();

      juridica.actualizarDatos(razonSocialMerge, tipoOrgMerge, rubroMerge, documentoMerge, representantesMerge, contactosMerge);
    } else {
      throw new DomainValidationException("Tipo de donante desconocido");
    }
  }

  //================== FUNCIONES AUXILIARES ================
  private static TipoDonante aTipoDonante(String valor) {
    if (valor == null || valor.isBlank()) return null;
    try {
      return TipoDonante.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DomainValidationException("El tipo de donante: " + valor + " no existe debe ser: " + Arrays.toString(TipoDonante.values()));
    }
  }

  private static Genero aGenero(String valor) {
    if (valor == null || valor.isBlank()) return null;
    try {
      return Genero.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DomainValidationException("El genero: " + valor + " no existe debe ser: " + Arrays.toString(Genero.values()));
    }
  }

  private static TipoOrganizacion aTipoOrganizacion(String valor) {
    if (valor == null || valor.isBlank()) return null;
    try {
      return TipoOrganizacion.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DomainValidationException("El tipo de Organización: " + valor + " no existe debe ser: " + Arrays.toString(TipoOrganizacion.values()));
    }
  }

}