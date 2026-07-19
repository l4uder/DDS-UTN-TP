package ar.edu.utn.frba.dds.donatrack.logistica.web.dto.externo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BeneficiarioDTO {
  @EqualsAndHashCode.Include
  private String id;
  private String razonSocial;
  private String direccion;
}
