package ar.edu.utn.frba.dds.donatrack.donaciones.cron;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.GeneradorRankings;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.RankingRepository;
import java.util.List;

public class ProcesoMatchmaking {
  public static void main(String[] args) {
    BeneficiarioRepository repoBeneficiarios = BeneficiarioRepository.getInstancia();
    DonacionRepository repoDonaciones = DonacionRepository.getInstancia();
    RankingRepository repoRankings = RankingRepository.getInstancia();

    List<Beneficiario> beneficiarios = repoBeneficiarios.buscarTodos();
    List<Donacion> donaciones = repoDonaciones.buscarTodoPorEstado(TipoEstadoDonacion.EN_DEPOSITO);

    GeneradorRankings generadorRankings = new GeneradorRankings(repoRankings);
    generadorRankings.asignar(donaciones, beneficiarios);

    System.out.println("acabamossss");
  }
}
