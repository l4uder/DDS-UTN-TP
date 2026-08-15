package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.representante;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.contacto.ContactoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.documento.DocumentoDto;
import java.util.List;

public record RepresentanteDto(
    String nombre,
    String apellido,
    DocumentoDto documentoDto,
    String direccion,
    List<ContactoDto> contactos
) { }
