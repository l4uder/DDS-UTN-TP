package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioResumenDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record DonacionResponse(
    String id,
    String descripcion,
    String estado,
    BeneficiarioResumenDto beneficiario,
    List<BienDto> bienes
) {
}
