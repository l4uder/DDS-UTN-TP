package ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;

public class DonacionEnTransito {
  private final String id;
  private final String descripcion;
  private final Beneficiario beneficiario;

  public DonacionEnTransito(String id, String descripcion, Beneficiario beneficiario) {
    if (id == null || id.isBlank()) {
      throw new DomainValidationException("La donación debe tener id");
    }
    if (beneficiario == null) {
      throw new DomainValidationException("La donación debe tener un beneficiario asignado");
    }
    this.id = id;
    this.descripcion = descripcion;
    this.beneficiario = beneficiario;
  }

  public String getId() { return id; }
  public String getDescripcion() { return descripcion; }
  public Beneficiario getBeneficiario() { return beneficiario; }
}