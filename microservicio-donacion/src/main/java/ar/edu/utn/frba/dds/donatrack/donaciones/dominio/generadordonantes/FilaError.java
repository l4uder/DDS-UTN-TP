package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes;

import lombok.Getter;

@Getter
public class FilaError {
  private Integer numeroFila;
  private String motivoError;

  public FilaError(Integer numeroFila, String motivoError) {
    this.numeroFila = numeroFila;
    this.motivoError = motivoError;
  }

}
