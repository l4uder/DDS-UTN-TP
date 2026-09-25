package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Juridica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.persona.Humana;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.util.List;

public class DonanteFactory {

  public static Donante crear(String tipoPersona, Documento documento,
                              String nombreCompleto, MedioContacto contactoPrincipal,
                              MedioContacto contactoSecundario) {
    String nombreLimpio = nombreCompleto.trim().replaceAll("\\s+", " ");
    return switch (tipoPersona.toUpperCase()) {
      case "HUMANA" -> {
        String[] partimosNombre = nombreLimpio.split(" ", 2);
        String nombre = partimosNombre[0];
        String apellido = partimosNombre.length > 1 ? partimosNombre[1] : null;
        yield new Humana(
            nombre,
            apellido,
            documento,
            null,
            null,
            "sin dirección viene del CSV",
            List.of(contactoPrincipal, contactoSecundario)
        );
      }
      case "JURIDICA" -> new Juridica(
          nombreLimpio,
          documento,
          null,
          null,
          List.of(new Representante(
              "sin nombre viene del CSV",
              null,
              null,
              null,
              List.of(contactoPrincipal, contactoSecundario)
          ))
      );
      default -> throw new DominioException("Tipo de persona desconocido en CSV: " + tipoPersona);
    };
  }

}