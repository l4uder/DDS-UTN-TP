package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Ranking {
  @Setter
  private String id;
  private Donacion donacion;
  private List<Beneficiario> candidatos;
  private Boolean estaVigente;
  private LocalDateTime fechaGeneracion;

  public Ranking(Donacion donacion, List<Beneficiario> candidatos) {
    this.donacion = donacion;
    this.candidatos = new ArrayList<>(candidatos);
    this.estaVigente = true;
    this.fechaGeneracion = LocalDateTime.now();
  }

  public void vencida() {
    this.estaVigente = false;
  }

}
