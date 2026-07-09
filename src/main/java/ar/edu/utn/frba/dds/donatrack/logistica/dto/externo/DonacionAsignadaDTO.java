package ar.edu.utn.frba.dds.donatrack.logistica.dto.externo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DonacionAsignadaDTO {
  private final String id;
  private final String descripcion;
  private final BeneficiarioDTO beneficiario;
}