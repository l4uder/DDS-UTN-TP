package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public class Documento {
  private TipoDocumento tipoDocumento;
  private String detalle;

  public Documento(TipoDocumento tipoDocumento, String detalle) {
    checkDatos(tipoDocumento, detalle);
    this.tipoDocumento = tipoDocumento;
    this.detalle = detalle;
  }

  private void checkDatos(TipoDocumento tipoDocumento, String detalle) {
    if (tipoDocumento == null) throw new DominioException("El campo 'tipo' de un documento es obligatorio, puede ser: " + Arrays.toString(TipoDocumento.values()));
    if (detalle == null || detalle.isBlank()) throw new DominioException("El campo 'numero' de un documento es obligatorio");
  }

  public Boolean esIgualA(Documento otrodocumento) {
    return this.tipoDocumento == otrodocumento.getTipoDocumento()
            && this.detalle.equalsIgnoreCase(otrodocumento.getDetalle());
  }

}
