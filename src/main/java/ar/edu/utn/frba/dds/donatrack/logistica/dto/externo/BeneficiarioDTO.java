package ar.edu.utn.frba.dds.donatrack.logistica.dto.externo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BeneficiarioDTO {
  @EqualsAndHashCode.Include
  private final String id;
  private final String razonSocial;
  private final String direccion;
}
