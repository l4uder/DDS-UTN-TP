package ar.edu.utn.frba.dds.donatrack.dominio.logistica;

import ar.edu.utn.frba.dds.donatrack.dominio.beneficiario.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.donatrack.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.ResultadoPlanificacion;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrquestadorLogistica {
  private static final int MAX_DONACIONES_POR_LOTE = 100;

  private List<Camion> camionesDisponibles;
  private List<Donacion> donacionesAsignadas;

  public OrquestadorLogistica(List<Camion> camionesDisponibles, List<Donacion> donacionesAsignadas) {
    this.camionesDisponibles = camionesDisponibles;
    this.donacionesAsignadas = donacionesAsignadas.stream()
        .filter(d -> d.getEstadoActual() == TipoEstadoDonacion.ASIGNACION_REALIZADA)
        .collect(Collectors.toList());
  }

  public List<Entrega> armarEntregasPendientes() {
    Map<EntidadBeneficiaria, List<Donacion>> agrupadas = donacionesAsignadas.stream()
        .collect(Collectors.groupingBy(Donacion::getEntidadAsignada));

    List<Entrega> entregas = new ArrayList<>();
    for (Map.Entry<EntidadBeneficiaria, List<Donacion>> grupo : agrupadas.entrySet()) {
      entregas.add(new Entrega(grupo.getKey(), grupo.getValue(), null)); // sin camión aún
    }
    return entregas;
  }

  public List<List<Entrega>> armarLotes(List<Entrega> entregas) {
    List<List<Entrega>> lotes = new ArrayList<>();
    List<Entrega> loteActual = new ArrayList<>();
    int contadorDonaciones = 0;

    for (Entrega e : entregas) {
      int cantidadDonaciones = e.getDonaciones().size();
      if (contadorDonaciones + cantidadDonaciones > MAX_DONACIONES_POR_LOTE && !loteActual.isEmpty()) {
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

  public List<Ruta> procesarResultadoPlanificacion(ResultadoPlanificacion resultado, LocalDate fecha) {
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