package ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "beneficiarios")
public class Beneficiario {
  @Id
  private String id;
  @Column(name = "razon_social")
  private String razonSocial;
  @Column(name = "direccion")
  private String direccion;

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