package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.contacto.ContactoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.documento.DocumentoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.representante.RepresentanteDto;
import java.util.List;

public record DonanteRequest(
    String tipo,
    DocumentoDto documento,
    // Persona Humana
    String nombre,
    String apellido,
    String fechaNacimiento,
    String genero,
    String direccion,
    List<ContactoDto> contactos,
    // Persona Jurídica
    String razonSocial,
    String tipoOrganizacion,
    String rubro,
    List<RepresentanteDto> representantes
) { }
