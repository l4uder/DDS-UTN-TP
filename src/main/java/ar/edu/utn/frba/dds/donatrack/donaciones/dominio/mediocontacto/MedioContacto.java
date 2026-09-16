package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table (name = "medio_contacto")
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn (name = "tipo_contacto", discriminatorType = DiscriminatorType.STRING)
public abstract class MedioContacto {

  @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;
  @Column (name = "es_principal")
  protected Boolean esPrincipal;
  @Column (name = "detalle")
  protected String detalle;

  public boolean getEsPrincipal() {
    return this.esPrincipal;
  }

  public abstract void enviarMensaje(String message);
  public abstract boolean esIgualA(MedioContacto otro);
}
