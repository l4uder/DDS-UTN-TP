package ar.edu.utn.frba.dds.donatrack.donaciones.dto.necesidad;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.NecesidadExtraordinaria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.NecesidadRecurrente;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Periodo;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;

public class NecesidadMapper {

  private NecesidadMapper() {
  }

  public static Necesidad aDominio(NecesidadRequest request) {
    validar(request);
    Subcategoria subcategoria = aSubcategoria(request);
    return switch (request.tipo().toUpperCase()) {
      case "RECURRENTE" -> new NecesidadRecurrente(
          subcategoria,
          request.descripcion(),
          request.cantidadPorPeriodo(),
          aPeriodo(request.periodo()));
      default -> new NecesidadExtraordinaria(
          subcategoria,
          request.descripcion(),
          request.cantidadRequerida());
    };
  }


  public static void actualizarDominio(Necesidad necesidad, NecesidadRequest request) {
    validar(request);
    String tipoActual = tipoDe(necesidad);
    if (!tipoActual.equalsIgnoreCase(request.tipo())) {
      throw new DomainValidationException(
          "La necesidad es de tipo " + tipoActual
              + " y el tipo no puede modificarse: eliminarla y crear una nueva");
    }
    if (necesidad instanceof NecesidadRecurrente recurrente) {
      recurrente.actualizarDatos(
          aSubcategoria(request),
          request.descripcion(),
          request.cantidadPorPeriodo(),
          aPeriodo(request.periodo()));
    } else if (necesidad instanceof NecesidadExtraordinaria extraordinaria) {
      extraordinaria.actualizarDatos(
          aSubcategoria(request),
          request.descripcion(),
          request.cantidadRequerida());
    }
  }

  public static void validar(NecesidadRequest request) {
    if (request.tipo() == null) {
      throw new DomainValidationException(
          "El campo 'tipo' es obligatorio (RECURRENTE o EXTRAORDINARIA)");
    }
    if (request.descripcion() == null || request.descripcion().isBlank()) {
      throw new DomainValidationException("El campo 'descripcion' es obligatorio");
    }
    if (request.categoria() == null || request.categoria().isBlank()
        || request.subcategoria() == null || request.subcategoria().isBlank()) {
      throw new DomainValidationException(
          "Los campos 'categoria' y 'subcategoria' son obligatorios");
    }
    switch (request.tipo().toUpperCase()) {
      case "RECURRENTE" -> {
        if (request.cantidadPorPeriodo() == null || request.cantidadPorPeriodo() <= 0) {
          throw new DomainValidationException(
              "Una necesidad recurrente necesita 'cantidadPorPeriodo' mayor a cero");
        }
        aPeriodo(request.periodo());
      }
      case "EXTRAORDINARIA" -> {
        if (request.cantidadRequerida() == null || request.cantidadRequerida() <= 0) {
          throw new DomainValidationException(
              "Una necesidad extraordinaria necesita 'cantidadRequerida' mayor a cero");
        }
      }
      default -> throw new DomainValidationException(
          "Tipo de necesidad invalido: " + request.tipo() + " (RECURRENTE o EXTRAORDINARIA)");
    }
  }

  public static NecesidadResponse aResponse(Necesidad necesidad) {
    boolean esRecurrente = necesidad instanceof NecesidadRecurrente;
    return new NecesidadResponse(
        necesidad.getId(),
        tipoDe(necesidad),
        necesidad.getDescripcion(),
        necesidad.getSubcategoria().getCategoria().getNombre(),
        necesidad.getSubcategoria().getNombre(),
        necesidad.getCantidadRecibida(),
        necesidad.esSatisfecha(),
        esRecurrente ? ((NecesidadRecurrente) necesidad).getCantidadPorPeriodo() : null,
        esRecurrente ? ((NecesidadRecurrente) necesidad).getPeriodo().name() : null,
        esRecurrente ? null : ((NecesidadExtraordinaria) necesidad).getCantidadRequerida());
  }

  private static String tipoDe(Necesidad necesidad) {
    return necesidad instanceof NecesidadRecurrente ? "RECURRENTE" : "EXTRAORDINARIA";
  }

  private static Subcategoria aSubcategoria(NecesidadRequest request) {
    return new Subcategoria(request.subcategoria(), new Categoria(request.categoria()));
  }

  private static Periodo aPeriodo(String valor) {
    if (valor == null) {
      throw new DomainValidationException(
          "Una necesidad recurrente necesita 'periodo' (DIARIO, SEMANAL o MENSUAL)");
    }
    try {
      return Periodo.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DomainValidationException(
          "Valor invalido para periodo: " + valor + " (DIARIO, SEMANAL o MENSUAL)");
    }
  }

}
