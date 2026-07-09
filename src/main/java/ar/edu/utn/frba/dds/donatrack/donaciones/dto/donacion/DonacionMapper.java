package ar.edu.utn.frba.dds.donatrack.donaciones.dto.donacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien.NoPerecedero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.tipobien.Perecedero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.EstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;

import java.util.List;

public class DonacionMapper {

  private DonacionMapper() {
  }

  public static Donacion aDominio(DonacionRequest request) {
    return new Donacion(aBienes(request.bienes()));
  }

  public static List<Bien> aBienes(List<BienDto> bienes) {
    if (bienes == null || bienes.isEmpty()) {
      throw new DomainValidationException("Una donación debe tener al menos un bien");
    }
    return bienes.stream().map(DonacionMapper::aBien).toList();
  }

  public static DonacionResponse aResponse(Donacion donacion) {
    return new DonacionResponse(
        donacion.getId(),
        donacion.getDescripcion(),
        donacion.getEstadoActual().name(),
        donacion.getBeneficiario() == null ? null : donacion.getBeneficiario().getRazonSocial(),
        donacion.getBienes().stream().map(DonacionMapper::aBienDto).toList());
  }

  public static EstadoDonacionDto aEstadoDto(EstadoDonacion estado) {
    return new EstadoDonacionDto(
        estado.getTipoEstado().name(),
        estado.getFecha(),
        estado.getDetalle());
  }

  private static Bien aBien(BienDto dto) {
    if (dto.tipo() == null) {
      throw new DomainValidationException(
          "Cada bien necesita 'tipo' (PERECEDERO o NO_PERECEDERO)");
    }
    if (dto.descripcion() == null || dto.cantidad() == null) {
      throw new DomainValidationException("Cada bien necesita 'descripcion' y 'cantidad'");
    }
    if (dto.categoria() == null || dto.subcategoria() == null) {
      throw new DomainValidationException("Cada bien necesita 'categoria' y 'subcategoria'");
    }

    UnidadMedida unidad = parseUnidadMedida(dto.unidadMedida());
    Subcategoria subcategoria = new Subcategoria(dto.subcategoria(), new Categoria(dto.categoria()));

    return switch (dto.tipo().toUpperCase()) {
      case "PERECEDERO" -> {
        if (dto.fechaVencimiento() == null) {
          throw new DomainValidationException(
              "Un bien perecedero necesita 'fechaVencimiento'");
        }
        yield Bien.crearPerecedero(dto.descripcion(), dto.cantidad(), unidad,
            dto.foto(), subcategoria, dto.fechaVencimiento());
      }
      case "NO_PERECEDERO" -> Bien.crearNoPerecedero(dto.descripcion(), dto.cantidad(), unidad,
          dto.foto(), subcategoria, Boolean.TRUE.equals(dto.usado()));
      default -> throw new DomainValidationException(
          "Tipo de bien invalido: " + dto.tipo() + " (PERECEDERO o NO_PERECEDERO)");
    };
  }

  private static BienDto aBienDto(Bien bien) {
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

  private static UnidadMedida parseUnidadMedida(String valor) {
    if (valor == null) {
      return UnidadMedida.SIN_UNIDAD;
    }
    try {
      return UnidadMedida.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DomainValidationException("Valor invalido para unidad de medida: " + valor);
    }
  }

}
