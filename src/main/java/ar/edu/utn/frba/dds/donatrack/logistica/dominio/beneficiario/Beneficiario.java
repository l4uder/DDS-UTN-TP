package ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;

public class Beneficiario {
  private final String id;
  private final String razonSocial;
  private final String direccion;

  public Beneficiario(String id, String razonSocial, String direccion) {
    if (id == null || id.isBlank()) {
      throw new DomainValidationException("El beneficiario debe tener id");
    }
    this.id = id;
    this.razonSocial = razonSocial;
    this.direccion = direccion;
  }

  public String getId() { return id; }
  public String getRazonSocial() { return razonSocial; }
  public String getDireccion() { return direccion; }

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