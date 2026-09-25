package ar.edu.utn.frba.dds.donatrack.donaciones.cron;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import java.util.List;

public class AusenciaBeneficiario {
  public static void main(String[] args) {
    DonanteRepository repoDonantes = DonanteRepository.getInstancia();

    List<Donante> donantesAusentes = repoDonantes.buscarAusentesPorMas(20);

    donantesAusentes.forEach(d -> d.recibirNotificacion(
        "vuelve a donar porfavor, eso ayuda a personas que lo necesitan"));

    System.out.println("acabamossss");
  }
}
