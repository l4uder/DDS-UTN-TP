package ar.edu.utn.frba.dds.donatrack.logistica.dominio.orquestadorlogistica;

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

public class OrquestadorLogistica {
  private static final int MAX_DONACIONES_POR_LOTE = 100;

  private List<Camion> camionesDisponibles;
  private List<DonacionAsignadaDTO> donacionesAllevar;

  public OrquestadorLogistica(List<Camion> camionesDisponibles,
                              List<DonacionAsignadaDTO> donacionesAllevar) {
    this.camionesDisponibles = camionesDisponibles;
    this.donacionesAllevar = donacionesAllevar;
  }

  public List<Entrega> armarEntregasPendientes() {
    Map<BeneficiarioDTO, List<DonacionAsignadaDTO>> agrupadasPorBeneficiario = donacionesAllevar.stream()
        .collect(Collectors.groupingBy(DonacionAsignadaDTO::getBeneficiario));

    List<Entrega> entregas = new ArrayList<>();
    agrupadasPorBeneficiario.forEach((beneficiario, donaciones) ->
        entregas.add(new Entrega(beneficiario, donaciones, null)));

    return entregas;
  }

  public List<List<Entrega>> armarLotesEntrega(List<Entrega> entregas) {
    List<List<Entrega>> lotes = new ArrayList<>();
    List<Entrega> loteActual = new ArrayList<>();
    int contadorDonaciones = 0;

    for (Entrega e : entregas) {
      int cantidadDonaciones = e.getDonaciones().size();
      if (contadorDonaciones + cantidadDonaciones > MAX_DONACIONES_POR_LOTE
          && !loteActual.isEmpty()) {
        lotes.add(loteActual);
        loteActual = new ArrayList<>();
        contadorDonaciones = 0;
      }
      loteActual.add(e);
      contadorDonaciones += cantidadDonaciones;
    }
    if (!loteActual.isEmpty()) {
      lotes.add(loteActual);
    }
    return lotes;
  }

  public List<Ruta> procesarResultadoPlanificacion(ResultadoPlanificacion resultado,
                                                   LocalDate fecha) {
    List<Ruta> rutasCreadas = new ArrayList<>();

    for (Map.Entry<Camion, List<Entrega>> entry : resultado.getEntregasPorCamion().entrySet()) {
      Camion camion = entry.getKey();
      List<Entrega> entregasOrdenadas = entry.getValue();

      for (Entrega e : entregasOrdenadas) {
        e.reasignarCamion(camion);
        e.confirmarListaParaEntregar();
      }

      Ruta ruta = new Ruta(camion, fecha, entregasOrdenadas);
      rutasCreadas.add(ruta);
    }

    return rutasCreadas;
  }

}
