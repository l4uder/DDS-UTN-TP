package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.asignador;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import java.util.ArrayList;
import java.util.List;

public class ResultadoAsignacion {
  private Donacion donacion;
  private List<Beneficiario> beneficiarios;

  public ResultadoAsignacion(Donacion donacion,  List<Beneficiario> beneficiarios) {
    this.donacion = donacion;
    this.beneficiarios = new ArrayList<>(beneficiarios);
  }
}
