package ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.BeneficiarioDTO;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.DonacionAsignadaDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlanificadorLogistico {
  private static final int MAX_DONACIONES_POR_LOTE = 100;

  public List<Entrega> armarEntregasPendientes(List<Donacion> donacionesAsignadas) {
    Map<Beneficiario, List<Donacion>> agrupadasPorBeneficiario =
        donacionesAsignadas.stream()
            .collect(Collectors.groupingBy(d -> d.getBeneficiario()));

    List<Entrega> entregas = new ArrayList<>();

    agrupadasPorBeneficiario.forEach(
        (beneficiario, donaciones) ->
            entregas.add(new Entrega( beneficiario, donaciones,null ))
    );

    return entregas;
  }


  public List<List<Entrega>> armarLotesEntrega(
      List<Entrega> entregas
  ) {

    List<List<Entrega>> lotes = new ArrayList<>();

    List<Entrega> loteActual = new ArrayList<>();

    int contadorDonaciones = 0;


    for (Entrega entrega : entregas) {

      int cantidadDonaciones =
          entrega.getDonaciones().size();


      if (contadorDonaciones + cantidadDonaciones > MAX_DONACIONES_POR_LOTE
          && !loteActual.isEmpty()) {

        lotes.add(loteActual);

        loteActual = new ArrayList<>();

        contadorDonaciones = 0;
      }


      loteActual.add(entrega);

      contadorDonaciones += cantidadDonaciones;
    }


    if (!loteActual.isEmpty()) {
      lotes.add(loteActual);
    }


    return lotes;
  }


  public List<Ruta> procesarResultadoPlanificacion(
      ResultadoPlanificacion resultado,
      LocalDate fecha
  ) {

    List<Ruta> rutasCreadas = new ArrayList<>();


    for (Map.Entry<Camion, List<Entrega>> entry :
        resultado.getEntregasPorCamion().entrySet()) {


      Camion camion = entry.getKey();

      List<Entrega> entregasOrdenadas =
          entry.getValue();


      for (Entrega entrega : entregasOrdenadas) {

        entrega.reasignarCamion(camion);

        entrega.confirmarListaParaEntregar();
      }


      Ruta ruta = new Ruta(
          camion,
          fecha,
          entregasOrdenadas
      );


      rutasCreadas.add(ruta);
    }


    return rutasCreadas;
  }
}