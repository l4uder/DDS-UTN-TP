package ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "choferes")
public class Chofer {
  @Id
  private String licenciaConducir;
  @Column(name = "nombre")
  private String nombre;
  @Column(name = "apellido")
  private String apellido;

  public Chofer(String nombre, String apellido, String licenciaConducir) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.licenciaConducir = licenciaConducir;
  }

}