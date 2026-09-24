package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Frecuencia;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.TipoNecesidad;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad.NecesidadRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad.NecesidadResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NecesidadMapper {

  public static Necesidad aDominio(NecesidadRequest request, Subcategoria subcategoria) {
    TipoNecesidad tipo = aTiponecesidad(request.tipo());
    UnidadMedida unidadMedida = aUnidadMedida(request.unidadMedida());
    return switch (tipo) {
      case RECURRENTE -> Necesidad.crearNecesidadRecurrente(
          request.descripcion(),
          subcategoria,
          unidadMedida,
          request.cantidadPorPeriodo(),
          aPeriodo(request.periodo()));
      case EXTRAORDINARIA -> Necesidad.crearNecesidadExtraordinaria(
          request.descripcion(),
          subcategoria,
          unidadMedida,
          request.cantidadRequerida());
      default -> throw new DominioException("Necesidad incorrecta, debe ser (RECURRENTE o EXTRAORDINARIA)");
    };
  }

  public static NecesidadResponse aDto(Necesidad necesidad) {
    NecesidadResponse.NecesidadResponseBuilder responseBuilder = NecesidadResponse.builder()
        .id(necesidad.getId())
        .tipo(necesidad.getTipo().name())
        .unidadMedida(necesidad.getUnidadMedida().name())
        .descripcion(necesidad.getDescripcion())
        .categoria(necesidad.getSubcategoria().getCategoria().getNombre())
        .subcategoria(necesidad.getSubcategoria().getNombre())
        .cantidadRecibida(necesidad.getCantidadRecibida())
        .estaSatisfecha(necesidad.estaSatisfecha());

    switch (necesidad.getTipo()) {
      case RECURRENTE -> {
        responseBuilder.cantidadPorPeriodo(necesidad.getCantidadRequerida());
        responseBuilder.periodo(necesidad.getFrecuencia().name());
      }
      case EXTRAORDINARIA ->
        responseBuilder.cantidadRequerida(necesidad.getCantidadRequerida());
      default -> throw new DominioException("Tipo de necesidad desconocido, o no actualizado el switch");
    }

    return responseBuilder.build();
  }

  public static List<NecesidadResponse> aDto(List<Necesidad> necesidades) {
    return necesidades.stream().map(NecesidadMapper::aDto).toList();
  }

  public static void actualizarDominio(Necesidad necesidad, NecesidadRequest request, Subcategoria subcategoriaNueva) {
    if (request.tipo() != null && !necesidad.getTipo().name().equalsIgnoreCase(request.tipo()))
      throw new IllegalArgumentException("No se puede modificar el tipo de la necesidad");

    Subcategoria subcategoriaMerge = request.subcategoria() != null ? subcategoriaNueva : necesidad.getSubcategoria();
    String descripcionMerge = request.descripcion() != null ? request.descripcion() : necesidad.getDescripcion();
    UnidadMedida unidadMedida = request.unidadMedida() != null ? aUnidadMedida(request.unidadMedida()) : necesidad.getUnidadMedida();
    switch (necesidad.getTipo()) {
        case RECURRENTE -> {
          Integer cantidadPorPeriodoMerge = request.cantidadPorPeriodo() != null ? request.cantidadPorPeriodo() : necesidad.getCantidadRequerida();
          Frecuencia periodoMerge = request.periodo() != null ? aPeriodo(request.periodo()) : necesidad.getFrecuencia();
          necesidad.actualizarDatosRecurrente(descripcionMerge, subcategoriaMerge, unidadMedida, cantidadPorPeriodoMerge, periodoMerge);
        }
        case EXTRAORDINARIA -> {
          Integer cantidadRequeridaMerge = request.cantidadRequerida() != null ? request.cantidadRequerida() : necesidad.getCantidadRequerida();
          necesidad.actualizarDatosExtraordinaria(descripcionMerge, subcategoriaMerge, unidadMedida, cantidadRequeridaMerge);
        }
        default -> throw new DominioException("Tipo de necesidad desconocido, o no actualizado el switch");
    }
  }

  //============== FUNCIONES AUXILIARES ==================
  private static TipoNecesidad aTiponecesidad(String valor) {
    if (valor ==null) throw new DominioException("Necesita 'tipo' valores posibles: " + Arrays.toString(TipoNecesidad.values()));
    try {
      return TipoNecesidad.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DominioException("El tipo: " + valor + " no existe, debe ser: " + Arrays.toString(Frecuencia.values()));
    }
  }

  private static Frecuencia aPeriodo(String valor) {
    if (valor == null) throw new DominioException("Una necesidad recurrente necesita 'periodo' puede ser: " + Arrays.toString(Frecuencia.values()));
    try {
      return Frecuencia.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DominioException("El periodo: " + valor + " no existe, debe ser: " + Arrays.toString(Frecuencia.values()));
    }
  }

  private static UnidadMedida aUnidadMedida(String valor) {
    if (valor == null) throw new DominioException("Necesita 'unidad_medida' valores posibles: " + Arrays.toString(UnidadMedida.values()));
    try {
      return UnidadMedida.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DominioException("La unidad de medida: " + valor + " no existe, debe ser: " + Arrays.toString(UnidadMedida.values()));
    }
  }

}
