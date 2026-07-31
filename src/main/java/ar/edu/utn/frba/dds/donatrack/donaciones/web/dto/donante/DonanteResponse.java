package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.contacto.ContactoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.documento.DocumentoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.representante.RepresentanteDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record DonanteResponse(
    String id,
    String tipo,
    DocumentoDto documento,
    // Campos persona Humana
    String nombre,
    String apellido,
    String años,
    String genero,
    String direccion,
    List<ContactoDto> contactos,
    // Campos persona Jurídica
    String razonSocial,
    String tipoOrganizacion,
    String rubro,
    List<RepresentanteDto> representantes
) { }
