package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.util.List;

public class DonanteFactory {

  public static Donante crear(String tipoPersona, Documento documento,
                              String nombreCompleto, MedioContacto contactoPrincipal,
                              MedioContacto contactoSecundario) {

    contactoPrincipal.setPrincipal(true);

    if (tipoPersona.equals("HUMANA")) {
      String[] partimosNombre = nombreCompleto.trim().split(" +", 2);
      String nombre = partimosNombre[0];
      String apellido = partimosNombre.length > 1 ? partimosNombre[1] : "";

      return new PersonaHumana(nombre, apellido, documento, null, null,
                              null, List.of(contactoPrincipal, contactoSecundario));
    }

    if (tipoPersona.equals("JURIDICA")) {
      return new PersonaJuridica(nombreCompleto, TipoOrganizacion.SIN_ESPECIFICAR, null,
                                  documento, null,
                                  List.of(contactoPrincipal, contactoSecundario));
    }

    throw new DomainValidationException("Tipo de persona invalido debe revisar: " + tipoPersona);
  }
}