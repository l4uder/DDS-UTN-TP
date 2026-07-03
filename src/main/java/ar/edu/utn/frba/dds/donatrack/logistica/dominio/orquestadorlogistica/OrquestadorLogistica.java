package ar.edu.utn.frba.dds.donatrack.logistica.dominio.orquestadorlogistica;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.OrquestadorException;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.Ruta;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrquestadorLogistica {
  private static final int MAX_DONACIONES_POR_LOTE = 100;

  private List<Camion> camionesDisponibles;
  private List<Donacion> donacionesAllevar;

  public OrquestadorLogistica(List<Camion> camionesDisponibles,
                              List<Donacion> donacionesAllevar) {
    this.camionesDisponibles = camionesDisponibles;
    if (donacionesAllevar.stream()
        .anyMatch(d -> d.getEstadoActual() != TipoEstadoDonacion.ASIGNACION_REALIZADA)) {
      throw new OrquestadorException("Solo podemos llevar donaciones asignadas");
    }
    this.donacionesAllevar = donacionesAllevar;
  }

  public List<Entrega> armarEntregasPendientes() {
    Map<Beneficiario, List<Donacion>> agrupadasPorBeneficiario = donacionesAllevar.stream()
        .collect(Collectors.groupingBy(Donacion::getBeneficiario));

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