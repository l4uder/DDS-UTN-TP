package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes.importadorcsv.ImportadorCsv;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes.importadorcsv.ResultadoImportacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import java.util.List;

public class GeneradorDonantesCsv {
  private DonanteRepository repoDonantes;
  private ImportadorCsv importador;

  public GeneradorDonantesCsv(DonanteRepository repoDonantes, ImportadorCsv importador) {
    this.repoDonantes = repoDonantes;
    this.importador = importador;
  }

  public List<FilaError> iniciarCarga(String rutaArchivo) {
    ResultadoImportacion resultado = importador.importarDesdeArchivo(rutaArchivo);
    resultado.getDonantes().forEach(d -> repoDonantes.guardar(d));

    return resultado.getRegistroFallas();
  }

}
