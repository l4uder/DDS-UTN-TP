package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ranking {
  private final Donacion donacion;
  private final List<Beneficiario> candidatos;
  private final LocalDateTime fechaGeneracion;

  public Ranking(Donacion donacion, List<Beneficiario> candidatos) {
    this.donacion = donacion;
    this.candidatos = new ArrayList<>(candidatos);
    this.fechaGeneracion = LocalDateTime.now();
  }

  public String getDonacionId() {
    return donacion.getId();
  }

  public List<Beneficiario> getCandidatos() {
    return new ArrayList<>(candidatos);
  }

  public LocalDateTime getFechaGeneracion() {
    return fechaGeneracion;
  }
}
