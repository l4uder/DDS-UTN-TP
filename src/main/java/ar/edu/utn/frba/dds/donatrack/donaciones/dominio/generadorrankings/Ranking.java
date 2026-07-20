package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.EstadoDonacion;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ranking {
  private String id;
  private Donacion donacion;
  private List<Beneficiario> candidatos;
  private EstadoRanking estado;
  private LocalDateTime fechaGeneracion;

  public Ranking(Donacion donacion, List<Beneficiario> candidatos) {
    this.donacion = donacion;
    this.candidatos = new ArrayList<>(candidatos);
    this.estado = EstadoRanking.VIGENTE;
    this.fechaGeneracion = LocalDateTime.now();
  }

  public String getId() {
    return this.id;
  }

  public Donacion getDonacion() {
    return this.donacion;
  }

  public List<Beneficiario> getCandidatos() {
    return new ArrayList<>(candidatos);
  }

  public LocalDateTime getFechaGeneracion() {
    return fechaGeneracion;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setEstado(EstadoRanking estado) {
    this.estado = estado;
  }

}
