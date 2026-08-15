package ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import lombok.Getter;

@Getter
public class DonacionEnTransito {
  private final String id;
  private final String descripcion;
  private final Beneficiario beneficiario;

  public DonacionEnTransito(String id, String descripcion, Beneficiario beneficiario) {
    validar(id, beneficiario);
    this.id = id;
    this.descripcion = descripcion;
    this.beneficiario = beneficiario;
  }

  private void validar(String id, Beneficiario beneficiario) {
    if (id == null || id.isBlank())
      throw new DominioException("La donación debe tener id");

    if (beneficiario == null)
      throw new DominioException("La donación debe tener un beneficiario asignado");
  }

}