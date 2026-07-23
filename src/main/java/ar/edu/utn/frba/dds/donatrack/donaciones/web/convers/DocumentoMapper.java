package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.documento.DocumentoDto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentoMapper {

  public static Documento aDominio(DocumentoDto documentoDto) {
    TipoDocumento tipoDocumento = aTipoDocumento(documentoDto.tipo());
    String detalle = documentoDto.numero();

    return new Documento(tipoDocumento, detalle);
  }

  public static DocumentoDto aDto(Documento documento) {
    return new DocumentoDto(
        documento.getTipoDocumento().name(),
        documento.getDetalle()
    );
  }

  //=========== FUNCIONES AUXILIARES =============
  private static TipoDocumento aTipoDocumento(String tipoDocumento) {
    try {
      return TipoDocumento.valueOf(tipoDocumento.toUpperCase());
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new DomainValidationException("El tipo de documento: " + tipoDocumento + " no existe, debe ser alguno de: " + Arrays.toString(TipoDocumento.values()));
    }
  }

}
