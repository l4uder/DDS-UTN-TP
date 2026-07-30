package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.util.List;

public class DonanteFactory {

  public static Donante crear(String tipoPersona, Documento documento,
                              String nombreCompleto, MedioContacto contactoPrincipal,
                              MedioContacto contactoSecundario) {
    //contactoPrincipal.setPrincipal(true);
    if (tipoPersona.equals("HUMANA")) {
      String[] partimosNombre = nombreCompleto.trim().split(" +", 2);
      String nombre = partimosNombre[0];
      String apellido = partimosNombre.length > 1 ? partimosNombre[1] : "";

      return Donante.personaHumana(nombre, apellido, documento, null, null,
                              null, List.of(contactoPrincipal, contactoSecundario));
    }

    if (tipoPersona.equals("JURIDICA")) {
      return Donante.personaJuridica(nombreCompleto, documento, TipoOrganizacion.SIN_ESPECIFICAR,
                                    null, List.of(new Representante("sinNombreVieneCSV", null, null, null, null, List.of(contactoPrincipal, contactoSecundario)))
                                     );
    }

    throw new DomainValidationException("Tipo de persona invalido debe revisar: " + tipoPersona);
  }

}