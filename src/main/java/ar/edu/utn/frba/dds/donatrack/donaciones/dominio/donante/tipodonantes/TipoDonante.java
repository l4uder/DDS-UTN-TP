package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.TipoPersona;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.util.List;

public interface TipoDonante {
  String getNombreCompleto();
  List<MedioContacto> getContactos();
  List<MedioContacto> getContactosPrincipales();
}
