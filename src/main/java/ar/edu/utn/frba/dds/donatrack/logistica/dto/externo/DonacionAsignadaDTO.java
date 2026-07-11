package ar.edu.utn.frba.dds.donatrack.logistica.dto.externo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonacionAsignadaDTO {
  private String id;
  private String descripcion;
  private BeneficiarioDTO beneficiario;
}