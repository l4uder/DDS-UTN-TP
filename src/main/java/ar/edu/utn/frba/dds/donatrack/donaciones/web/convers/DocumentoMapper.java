package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoPersona;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.documento.DocumentoDto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.ValidacionDominioException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentoMapper {

  public static Documento aDominio(DocumentoDto documentoDto, TipoPersona tipoPersona) {
    if (documentoDto == null) return null;

    TipoDocumento tipoDocumento = aTipoDocumento(documentoDto.tipo(), tipoPersona);
    String detalle = documentoDto.numero();

    return new Documento(tipoDocumento, detalle);
  }

  public static DocumentoDto aDto(Documento documento) {
    if (documento == null) return null;
    return new DocumentoDto(
        documento.getTipoDocumento().name(),
        documento.getDetalle()
    );
  }

  //=========== FUNCIONES AUXILIARES =============
  private static TipoDocumento aTipoDocumento(String tipoDocumento, TipoPersona tipoPersona) {
    if (tipoDocumento == null || tipoDocumento.isBlank()) {
      throw new ValidacionDominioException("El 'tipo' de un documento es obligatorio, debe ser alguno de: " + TipoDocumento.values(tipoPersona));
    }
    try {
      return TipoDocumento.valueOf(tipoDocumento.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ValidacionDominioException("El tipo de documento: " + tipoDocumento + " no existe, debe ser alguno de: " + TipoDocumento.values(tipoPersona));
    }
  }

}
