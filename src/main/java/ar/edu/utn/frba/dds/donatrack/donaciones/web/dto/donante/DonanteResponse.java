package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donante;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.comun.ContactoDto;
import java.time.LocalDate;
import java.util.List;

public record DonanteResponse(
    String id,
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
