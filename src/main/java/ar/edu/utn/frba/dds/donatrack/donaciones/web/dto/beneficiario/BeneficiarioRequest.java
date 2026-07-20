package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.contacto.ContactoDto;
import java.util.List;

public record BeneficiarioRequest(
    String razonSocial,
    String direccion,
    List<ContactoDto> contactos
) {
}
