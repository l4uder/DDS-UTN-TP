package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes.importadorcsv.ImportadorCsv;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadordonantes.importadorcsv.ResultadoImportacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class GeneradorDonantesCsv implements WithSimplePersistenceUnit {
  private DonanteRepository repoDonantes;
  private ImportadorCsv importador;

  public GeneradorDonantesCsv(DonanteRepository repoDonantes, ImportadorCsv importador) {
    this.repoDonantes = repoDonantes;
    this.importador = importador;
  }

  public List<FilaError> iniciarCarga(String rutaArchivo) {
    ResultadoImportacion resultado = importador.importarDesdeArchivo(rutaArchivo);
    beginTransaction();
    resultado.getDonantes().forEach(d -> repoDonantes.guardar(d));
    commitTransaction();

    return resultado.getRegistroFallas();
  }

}
