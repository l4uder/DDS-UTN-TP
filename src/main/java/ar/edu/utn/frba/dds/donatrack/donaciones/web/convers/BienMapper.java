package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien.Perecedero;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.bien.BienDto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BienMapper {

  private static Bien aDominio(BienDto bienDto) {
    if (bienDto.tipo() == null) throw new DominioException( "Cada bien necesita 'tipo' (PERECEDERO o NO_PERECEDERO)");
    if (bienDto.descripcion() == null || bienDto.cantidad() == null) throw new DominioException("Cada bien necesita 'descripcion' y 'cantidad'");
    if (bienDto.categoria() == null || bienDto.subcategoria() == null) throw new DominioException("Cada bien necesita 'categoria' y 'subcategoria'");

    UnidadMedida unidad = aUnidadMedida(bienDto.unidadMedida());
    Subcategoria subcategoria = new Subcategoria(bienDto.subcategoria(), new Categoria(bienDto.categoria()));

    return switch (bienDto.tipo().toUpperCase()) {
      case "PERECEDERO" -> {
        if (bienDto.fechaVencimiento() == null) {
          throw new DominioException(
              "Un bien perecedero necesita 'fechaVencimiento'");
        }
        yield Bien.crearPerecedero(bienDto.descripcion(), bienDto.cantidad(), unidad,
            bienDto.foto(), subcategoria, bienDto.fechaVencimiento());
      }
      case "NO_PERECEDERO" -> Bien.crearNoPerecedero(bienDto.descripcion(), bienDto.cantidad(), unidad,
          bienDto.foto(), subcategoria, Boolean.TRUE.equals(bienDto.usado()));
      default -> throw new DominioException(
          "El tipo de bien: " + bienDto.tipo() + " no existe, debe ser: PERECEDERO o NO_PERECEDERO ");
    };
  }

  public static BienDto aDto(Bien bien) {
    return new BienDto(
        bien.getTipoBien().toString(),
        bien.getDescripcion(),
        bien.getCantidad(),
        bien.getUnidadMedida().name(),
        bien.getFoto(),
        bien.getSubcategoria().getCategoria().getNombre(),
        bien.getSubcategoria().getNombre(),
        bien.getTipoBien() instanceof Perecedero p ? p.getFechaVencimiento() : null,
        bien.getTipoBien() instanceof NoPerecedero np ? np.getEstaUsado() : null
    );
  }

  public static List<Bien> aDominio(List<BienDto> bienes) {
    if (bienes == null || bienes.isEmpty()) throw new DominioException("Una donación debe tener al menos un bien");

    return bienes.stream().map(BienMapper::aDominio).toList();
  }

  public static List<BienDto> aDto(List<Bien> bienes) {
    if (bienes == null || bienes.isEmpty()) throw new DominioException("Una donación debe tener al menos un bien");

    return bienes.stream().map(BienMapper::aDto).toList();
  }


  //========= FUNCIONES AUXILIARES ==============
  private static UnidadMedida aUnidadMedida(String valor) {
    if (valor == null) return UnidadMedida.UNIDADES;
    try {
      return UnidadMedida.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DominioException("La unidad de medida: " + valor + " no existe, debe ser: " + Arrays.toString(UnidadMedida.values()));
    }
  }

}
