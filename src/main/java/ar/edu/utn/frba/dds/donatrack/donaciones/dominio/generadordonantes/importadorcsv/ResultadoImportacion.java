package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes.importadorcsv;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes.FilaError;
import java.util.List;
import lombok.Getter;

@Getter
public class ResultadoImportacion{
  private List<Donante> donantes;
  private List<FilaError> registroFallas;

  public ResultadoImportacion(List<Donante> donantes, List<FilaError> registroFallas) {
    this.donantes = donantes;
    this.registroFallas = registroFallas;
  }

}
