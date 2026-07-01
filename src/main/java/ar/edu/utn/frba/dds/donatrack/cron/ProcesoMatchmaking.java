package ar.edu.utn.frba.dds.donatrack.cron;

import ar.edu.utn.frba.dds.donatrack.dominio.asignador.Asignador;
import ar.edu.utn.frba.dds.donatrack.dominio.asignador.ResultadoAsignacion;
import ar.edu.utn.frba.dds.donatrack.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.persistencia.DonacionRepository;
import java.util.List;

public class ProcesoMatchmaking {
  public static void main(String[] args) {
    BeneficiarioRepository repoBeneficiarios = BeneficiarioRepository.getInstancia();
    DonacionRepository repoDonaciones = DonacionRepository.getInstancia();

    List<Beneficiario> beneficiarios = repoBeneficiarios.buscarTodos();
    List<Donacion> donaciones = repoDonaciones.buscarDonacionesEnDeposito();

    Asignador asignador = new Asignador();

    List<ResultadoAsignacion> asignaciones = asignador.asignar(donaciones, beneficiarios);
    System.out.println("acabamossss");
  }
}
