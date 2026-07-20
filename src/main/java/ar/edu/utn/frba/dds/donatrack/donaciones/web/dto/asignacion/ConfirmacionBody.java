package ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmacionBody {
  private String estado;
  private String beneficiarioId;

  public ConfirmacionBody(String estado, String beneficiarioId) {
    this.estado = estado;
    this.beneficiarioId = beneficiarioId;
  }
}
