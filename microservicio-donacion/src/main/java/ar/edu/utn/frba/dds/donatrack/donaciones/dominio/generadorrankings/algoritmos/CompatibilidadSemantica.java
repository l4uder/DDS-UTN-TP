package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.algoritmos;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompatibilidadSemantica extends AlgoritmoMatchmaking {

  @Override
  protected Map<Beneficiario, Integer> calcularPuntaje(Donacion donacion, List<Beneficiario> beneficiarios) {
    return beneficiarios.stream()
        .map(b -> Map.entry(b, calcularnecesidadesCubiertas(b, donacion)))
        .filter(entry -> entry.getValue() >= 1)
        //Solo guardamos aquellos beneficiarios a quien les sirve la donacion
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  protected Comparator<Map.Entry<Beneficiario, Integer>> modoOrdenamiento() {
    // ordena de mayor a menor
    return Map.Entry.<Beneficiario, Integer>comparingByValue().reversed();
  }

  //====================  FUNCIONES AUXILIARES =====================
  private Integer calcularnecesidadesCubiertas(Beneficiario beneficiario, Donacion donacion) {
    List<Necesidad> necesidadesBeneficiario = beneficiario.getNecesidades();

    List<Necesidad> necesidadesCubiertas = necesidadesBeneficiario.stream()
        .filter(n -> { if(n.estaSatisfecha()) return false;
          double cantidadAportada = calculoAportacion(donacion, n);
          double cantidadNecesaria = n.getCantidadFaltanteEnMenorMedida();
          return cantidadAportada >= cantidadNecesaria;
        }).toList();

    return necesidadesCubiertas.size();
  }

  private double calculoAportacion(Donacion donacion, Necesidad necesidad) {
    return donacion.getBienes().stream()
        .filter(b -> b.getSubcategoria().esIgual(necesidad.getSubcategoria()))
        .mapToDouble(b -> b.getCantidadMenorMedida())
        .sum();
  }

}