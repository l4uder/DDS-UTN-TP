package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table (name = "rankings")
public class Ranking {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_ranking")
  private Long id;
  @Transient
  private Donacion donacion;
  @ManyToMany
  @JoinTable (
      name = "ranking_x_beneficiario",
      joinColumns = @JoinColumn (name = "id_ranking"),
      inverseJoinColumns = @JoinColumn (name = "id_beneficiario")
  )
  @OrderColumn (name = "orden_prioridad") // Ver @OrderBy !!!
  private List<Beneficiario> candidatos;
  @Column (name = "esta_vigente")
  private Boolean estaVigente;
  @Column (name = "fecha_generacion")
  private LocalDateTime fechaGeneracion;

  public Ranking(Donacion donacion, List<Beneficiario> candidatos) {
    this.donacion = donacion;
    this.candidatos = new ArrayList<>(candidatos);
    this.estaVigente = true;
    this.fechaGeneracion = LocalDateTime.now();
  }

  public void invalidar() {
    this.estaVigente = false;
  }

}
