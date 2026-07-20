package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.contacto.ContactoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.documento.DocumentoDto;
import java.time.LocalDate;
import java.util.List;

public record DonanteRequest(
    String tipo,
    DocumentoDto documento,
    List<ContactoDto> contactos,
    String nombre,
    String apellido,
    LocalDate fechaNacimiento,
    String genero,
    String direccion,
    String razonSocial,
    String tipoOrganizacion,
    String rubro
) {
}
