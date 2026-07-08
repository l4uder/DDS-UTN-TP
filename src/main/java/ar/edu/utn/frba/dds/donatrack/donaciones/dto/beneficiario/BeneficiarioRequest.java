package ar.edu.utn.frba.dds.donatrack.donaciones.dto.beneficiario;

import ar.edu.utn.frba.dds.donatrack.donaciones.dto.comun.ContactoDto;
import java.util.List;

public record BeneficiarioRequest(
    String razonSocial,
    String direccion,
    List<ContactoDto> contactos
) {
}
