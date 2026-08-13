package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes;

import lombok.Getter;

@Getter
public class FilaError {
  Integer numeroFila;
  String motivoError;

  public FilaError(Integer numeroFila, String motivoError) {
    this.numeroFila = numeroFila;
    this.motivoError = motivoError;
  }

}
