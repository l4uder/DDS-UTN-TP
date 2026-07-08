package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ranking {
  private final String donacionId;
  private final List<Beneficiario> candidatos;
  private final LocalDateTime fechaGeneracion;

  public Ranking(String donacionId, List<Beneficiario> candidatos) {
    this.donacionId = donacionId;
    this.candidatos = new ArrayList<>(candidatos);
    this.fechaGeneracion = LocalDateTime.now();
  }

  public String getDonacionId() {
    return donacionId;
  }

  public List<Beneficiario> getCandidatos() {
    return new ArrayList<>(candidatos);
  }

  public LocalDateTime getFechaGeneracion() {
    return fechaGeneracion;
  }
}
