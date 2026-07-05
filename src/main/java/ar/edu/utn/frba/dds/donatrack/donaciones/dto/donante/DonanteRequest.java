package ar.edu.utn.frba.dds.donatrack.donaciones.dto.donante;

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
