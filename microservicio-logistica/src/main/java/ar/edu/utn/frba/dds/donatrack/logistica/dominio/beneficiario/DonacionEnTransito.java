package ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class DonacionEnTransito {
  @Column(name = "donacion_id")
  private String id;

  @Column(name = "donacion_descripcion")
  private String descripcion;

  @ManyToOne
  @JoinColumn(name = "beneficiario_id")
  private Beneficiario beneficiario;

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