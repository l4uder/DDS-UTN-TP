package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.NecesidadExtraordinaria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.NecesidadRecurrente;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Periodo;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad.NecesidadRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad.NecesidadResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.ValidacionDominioException;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NecesidadMapper {

  public static Necesidad aDominio(NecesidadRequest request) {
    if (request.tipo() == null) throw new ValidacionDominioException("El campo 'tipo' es obligatorio (RECURRENTE o EXTRAORDINARIA)");

    Subcategoria subcategoria = aSubcategoria(request);
    UnidadMedida unidadMedida = aUnidadMedida(request.unidadMedida());
    return switch (request.tipo().toUpperCase()) {
      case "RECURRENTE" -> new NecesidadRecurrente(
          subcategoria,
          unidadMedida,
          request.descripcion(),
          request.cantidadPorPeriodo(),
          aPeriodo(request.periodo()));
      case "EXTRAORDINARIA" -> new NecesidadExtraordinaria(
          subcategoria,
          unidadMedida,
          request.descripcion(),
          request.cantidadRequerida());
      default -> throw new ValidacionDominioException("Necesidad incorrecta, debe ser (RECURRENTE o EXTRAORDINARIA)");
    };
  }

  public static NecesidadResponse aDto(Necesidad necesidad) {
    NecesidadResponse.NecesidadResponseBuilder responseBuilder = NecesidadResponse.builder()
        .id(necesidad.getId())
        .tipo(necesidad.getTipo())
        .unidadMedida(necesidad.getUnidadMedida().name())
        .descripcion(necesidad.getDescripcion())
        .categoria(necesidad.getSubcategoria().getCategoria().getNombre())
        .subcategoria(necesidad.getSubcategoria().getNombre())
        .cantidadRecibida(necesidad.getCantidadRecibida())
        .estaSatisfecha(necesidad.estaSatisfecha());

    if (necesidad instanceof NecesidadRecurrente nr) {
      responseBuilder.cantidadPorPeriodo(nr.getCantidadPorPeriodo());
      responseBuilder.periodo(nr.getPeriodo().name());
    } else if (necesidad instanceof NecesidadExtraordinaria ne) {
      responseBuilder.cantidadRequerida(ne.getCantidadRequerida());
    } else {
      throw new IllegalStateException("Tipo de necesidad desconocido");
    }

    return responseBuilder.build();
  }

  public static List<NecesidadResponse> aDto(List<Necesidad> necesidades) {
    return necesidades.stream().map(NecesidadMapper::aDto).toList();
  }

  public static void actualizarDominio(Necesidad necesidad, NecesidadRequest request) {
    if (request.tipo() != null && !necesidad.getTipo().equalsIgnoreCase(request.tipo())) throw new IllegalArgumentException("No se puede modificar el tipo de la necesidad");

    Subcategoria subcategoriaMerge = request.subcategoria() != null ? aSubcategoria(request) : necesidad.getSubcategoria();
    String descripcionMerge = request.descripcion() != null ? request.descripcion() : necesidad.getDescripcion();
    UnidadMedida unidadMedida = request.unidadMedida() != null ? aUnidadMedida(request.unidadMedida()) : necesidad.getUnidadMedida();
    if (necesidad instanceof NecesidadRecurrente necesidadR) {
      Integer cantidadPorPeriodoMerge = request.cantidadPorPeriodo() != null ? request.cantidadPorPeriodo() : necesidadR.getCantidadPorPeriodo();
      Periodo periodoMerge = request.periodo() != null ? aPeriodo(request.periodo()) : necesidadR.getPeriodo();
      necesidadR.actualizarDatos(subcategoriaMerge, unidadMedida, descripcionMerge, cantidadPorPeriodoMerge, periodoMerge);
    } else if (necesidad instanceof NecesidadExtraordinaria necesidadE) {
      Integer cantidadRequeridaMerge = request.cantidadRequerida() != null ? request.cantidadRequerida() : necesidadE.getCantidadRequerida();
      necesidadE.actualizarDatos(subcategoriaMerge, unidadMedida, descripcionMerge, cantidadRequeridaMerge);
    } else {
      throw new IllegalStateException("Tipo de necesidad desconocido");
    }
  }

  //============== FUNCIONES AUXILIARES ==================
  //Todo despues quitar esta función y agregar un repo de subcategorías
  private static Subcategoria aSubcategoria(NecesidadRequest request) {
    return new Subcategoria(request.subcategoria(), new Categoria(request.categoria()));
  }

  private static Periodo aPeriodo(String valor) {
    if (valor == null) throw new ValidacionDominioException("Una necesidad recurrente necesita 'periodo' puede ser: " + Arrays.toString(Periodo.values()));
    try {
      return Periodo.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ValidacionDominioException("El periodo: " + valor + " no existe, debe ser: " + Arrays.toString(Periodo.values()));
    }
  }

  private static UnidadMedida aUnidadMedida(String valor) {
    if (valor == null) throw new ValidacionDominioException("Necesita 'unidadMedida' valores posibles: " + Arrays.toString(UnidadMedida.values()));
    try {
      return UnidadMedida.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ValidacionDominioException("La unidad de medida: " + valor + " no existe, debe ser: " + Arrays.toString(UnidadMedida.values()));
    }
  }

}
