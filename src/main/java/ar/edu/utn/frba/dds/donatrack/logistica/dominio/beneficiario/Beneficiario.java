package ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import lombok.Getter;

@Getter
public class Beneficiario {
  private final String id;
  private final String razonSocial;
  private final String direccion;

  public Beneficiario(String id, String razonSocial, String direccion) {
    validar(id);
    this.id = id;
    this.razonSocial = razonSocial;
    this.direccion = direccion;
  }

  private void validar(String id) {
    if (id == null || id.isBlank())
      throw new DominioException("El beneficiario debe tener id");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Beneficiario otro)) return false;
    return id.equals(otro.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}