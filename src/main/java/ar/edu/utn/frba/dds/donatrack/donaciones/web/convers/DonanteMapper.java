package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.persona.Humana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Juridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoPersona;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante.DonanteResumenResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.ValidacionDominioException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DonanteMapper {

  public static Donante aDominio(DonanteRequest request) {
    TipoPersona tipo = aTipoPersona(request.tipo());

    return switch (tipo) {
      case HUMANA -> Donante.personaHumana(
          request.nombre(),
          request.apellido(),
          DocumentoMapper.aDominio(request.documento(), TipoPersona.HUMANA),
          aFecha(request.fechaNacimiento()),
          aGenero(request.genero()),
          request.direccion(),
          ContactoMapper.aDominio(request.contactos()));
      case JURIDICA -> Donante.personaJuridica(
          request.razonSocial(),
          DocumentoMapper.aDominio(request.documento(), TipoPersona.JURIDICA),
          aTipoOrganizacion(request.tipoOrganizacion()),
          request.rubro(),
          RepresentanteMapper.aDominio(request.representantes()));
    };
  }

  public static DonanteResponse aDto(Donante donante) {
    DonanteResponse.DonanteResponseBuilder dtoBuild = DonanteResponse.builder();
    TipoPersona tipo = donante.getTipoPersona();

    dtoBuild.id(donante.getId());
    dtoBuild.tipo(tipo.name());
    dtoBuild.documento(DocumentoMapper.aDto(donante.getDocumento()));
    switch (tipo) {
      case HUMANA -> {
        Humana humana = (Humana) donante.getTipoDonante();
        dtoBuild.nombre(humana.getNombre());
        dtoBuild.apellido(humana.getApellido());
        dtoBuild.años(humana.getEdad() + " años");
        dtoBuild.genero(humana.getGenero().name());
        dtoBuild.direccion(humana.getDireccion());
        dtoBuild.contactos(ContactoMapper.aDto(humana.getContactos()));
      }
      case JURIDICA -> {
        Juridica juridica = (Juridica) donante.getTipoDonante();
        dtoBuild.razonSocial(juridica.getRazonSocial());
        dtoBuild.tipoOrganizacion(juridica.getTipoOrganizacion().name());
        dtoBuild.rubro(juridica.getRubro());
        dtoBuild.representantes(RepresentanteMapper.aDto(juridica.getRepresentantes()));
      }
      default -> throw new ValidacionDominioException("Tipo de donante no soportado, o nuevo");
    }

    return dtoBuild.build();
  }

  public static DonanteResumenResponse aDtoResumen(Donante donante) {
    return new DonanteResumenResponse(
        donante.getId(),
        donante.getTipoPersona().name(),
        donante.getNombreCompleto());
  }

  public static void actualizarDesdeRequest(Donante donante, DonanteRequest request) {
    if (request.tipo() != null) {
      //TipoDonante nuevoTipo = parseEnum(TipoDonante.class, request.tipo(), "tipo de donante");
      //if (donante.getTipo() != nuevoTipo) throw new DomainValidationException("No se puede modificar el tipo de un donante existente.");
      throw new ValidacionDominioException("No puede modificar el tipo de un donante");
    }

    Documento documentoMerge = request.documento() != null ? DocumentoMapper.aDominio(request.documento(), donante.getTipoPersona()) : donante.getDocumento();

    switch (donante.getTipoPersona()) {
      case HUMANA -> {
        Humana humana = (Humana) donante.getTipoDonante();
        String nombreMerge = request.nombre() != null ? request.nombre() : humana.getNombre();
        String apellidoMerge = request.apellido() != null ? request.apellido() : humana.getApellido();
        LocalDate nacimientoMerge = request.fechaNacimiento() != null ? aFecha(request.fechaNacimiento()) : humana.getFechaNacimiento();
        String direccionMerge = request.direccion() != null ? request.direccion() : humana.getDireccion();
        Genero generoMerge = request.genero() != null ? aGenero(request.genero()) : humana.getGenero();
        List<MedioContacto> contactosMerge = request.contactos() != null ? ContactoMapper.aDominio(request.contactos()) : humana.getContactos();

        donante.actualizarDatosHumana(nombreMerge, apellidoMerge, documentoMerge, nacimientoMerge, generoMerge, direccionMerge, contactosMerge);
      }
      case JURIDICA -> {
        Juridica juridica = (Juridica) donante.getTipoDonante();
        String razonSocialMerge = request.razonSocial() != null ? request.razonSocial() : juridica.getRazonSocial();
        String rubroMerge = request.rubro() != null ? request.rubro() : juridica.getRubro();
        TipoOrganizacion tipoOrgMerge = request.tipoOrganizacion() != null ? aTipoOrganizacion(request.tipoOrganizacion()) : juridica.getTipoOrganizacion();
        List<Representante> representantesMerge = request.representantes() != null ? RepresentanteMapper.aDominio(request.representantes()) : juridica.getRepresentantes();

        donante.actualizarDatosJuridica(razonSocialMerge, documentoMerge, tipoOrgMerge, rubroMerge, representantesMerge);
      }
      default -> throw new ValidacionDominioException("Tipo de donante no soportado, o nuevo");
    }
  }

  //================== FUNCIONES AUXILIARES ================
  private static TipoPersona aTipoPersona(String valor) {
    if (valor == null || valor.isBlank()) {
      throw new ValidacionDominioException("El campo 'tipo' es obligatorio. Debe indicar el tipo de donante: " + Arrays.toString(TipoPersona.values()));    }
    try {
      return TipoPersona.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ValidacionDominioException("El tipo de donante: " + valor + " no existe debe ser: " + Arrays.toString(TipoPersona.values()));
    }
  }

  private static Genero aGenero(String valor) {
    if (valor == null || valor.isBlank()) return null;
    try {
      return Genero.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ValidacionDominioException("El genero: " + valor + " no existe debe ser: " + Arrays.toString(Genero.values()));
    }
  }

  private static TipoOrganizacion aTipoOrganizacion(String valor) {
    if (valor == null || valor.isBlank()) return null;
    try {
      return TipoOrganizacion.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ValidacionDominioException("El tipo de Organización: " + valor + " no existe debe ser: " + Arrays.toString(TipoOrganizacion.values()));
    }
  }

  private static LocalDate aFecha(String valor) {
    if (valor == null || valor.isBlank()) return null;
    try {
      return LocalDate.parse(valor); // Espera el formato YYYY-MM-DD
    } catch (DateTimeParseException e) {
      throw new ValidacionDominioException("El formato de la fecha de nacimiento es inválido. Debe ser AAAA-MM-DD.");
    }
  }

}