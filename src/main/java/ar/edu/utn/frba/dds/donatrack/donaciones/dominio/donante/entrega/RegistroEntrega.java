package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.entrega;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Registro_Entregas")
public class RegistroEntrega {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "fecha")
  private LocalDateTime fecha;
  @Column(name = "descripcion_general")
  private String descripcionGeneral;
  //  @OneToMany
  //  @JoinColumn(name = "id_registro_entrega")
  @Transient
  private List<Bien> bienes;
  @ManyToOne
  @JoinColumn(name = "id_donante")
  private Donante donante; //Doble Referencia

  public RegistroEntrega(String descripcionGeneral, List<Bien> bienes, Donante donante) {
    this.fecha = LocalDateTime.now();
    this.donante = donante;
    this.descripcionGeneral = descripcionGeneral;
    this.bienes = new ArrayList<>(bienes);
  }

}