package ar.edu.utn.frba.dds.donatrack.cron;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.orquestadorlogistica.OrquestadorLogistica;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.EntregaRepository;

import java.util.List;

public class ProcesoLogistica {
  public static void main(String[] args) {
    DonacionRepository repoDonaciones = DonacionRepository.getInstancia();
    CamionRepository repoCamiones = CamionRepository.getInstancia();
    EntregaRepository repoEntregas = EntregaRepository.getInstancia();

    List<Donacion> donacionesAsignadas = repoDonaciones.buscarDonacionesAsignadas();
    List<Camion> camiones = repoCamiones.buscarTodos();

    OrquestadorLogistica orquestador = new OrquestadorLogistica(camiones, donacionesAsignadas);

    List<Entrega> entregas = orquestador.armarEntregasPendientes();
    List<List<Entrega>> lotes = orquestador.armarLotesEntrega(entregas);

    entregas.forEach(repoEntregas::guardar);

    // cada lote se manda al componente externo via HTTP (el callback completa el flujo)
    lotes.forEach(lote -> {
      System.out.println("Enviando lote de " + lote.size() + " entregas al planificador externo");
      // TODO: llamada HTTP al componente externo
    });
  }
}