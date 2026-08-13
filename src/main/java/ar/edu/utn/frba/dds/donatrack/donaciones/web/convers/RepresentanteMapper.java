package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoPersona;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.representante.RepresentanteDto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.Arrays;
import java.util.List;

public class RepresentanteMapper {

  public static Representante aDominio(RepresentanteDto representanteDto){
    return new Representante(
        representanteDto.nombre(),
        representanteDto.apellido(),
        DocumentoMapper.aDominio(representanteDto.documentoDto(), TipoPersona.HUMANA),
        aGenero(representanteDto.genero()),
        representanteDto.direccion(),
        ContactoMapper.aDominio(representanteDto.contactos())
    );
  }

  public static RepresentanteDto aDto(Representante representante) {
    return new RepresentanteDto(
        representante.getNombre(),
        representante.getApellido(),
        DocumentoMapper.aDto(representante.getDocumento()),
        representante.getGenero().name(),
        representante.getDireccion(),
        ContactoMapper.aDto(representante.getContactos())
    );
  }

  public static List<Representante> aDominio(List<RepresentanteDto> representantesDto) {
    return representantesDto.stream().map(RepresentanteMapper::aDominio).toList();
  }

  public static List<RepresentanteDto> aDto(List<Representante> representantes) {
    return representantes.stream().map(RepresentanteMapper::aDto).toList();
  }

  //===================== FUNCIONES AUXILIARES =====================
  private static Genero aGenero(String valor) {
    if (valor == null || valor.isBlank()) return null;
    try {
      return Genero.valueOf(valor.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DominioException("El genero: " + valor + " no existe debe ser: " + Arrays.toString(Genero.values()));
    }
  }

}
