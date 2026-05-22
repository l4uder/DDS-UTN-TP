package ar.edu.utn.frba.dds.donatrack.donante;

import ar.edu.utn.frba.dds.donatrack.share.MedioContacto;

import java.util.List;

public class DonanteSimpleFactory {

    public static Donante crear(String tipoPersona, Documento documento, String nombreCompleto, MedioContacto contactoPrincipal, MedioContacto contactoSecundario) {
        if(tipoPersona.equals("HUMANA")) {
            String[] partimosNombre = nombreCompleto.trim().split(" +", 2);
            String nombre = partimosNombre[0];
            String apellido = partimosNombre.length > 1 ? partimosNombre[1] : "";
            List<MedioContacto> contactosSecundarios = contactoSecundario == null ? null : List.of(contactoSecundario);

            return new PersonaHumana(
                    nombre,
                    apellido,
                    documento,
                    null,
                    null,
                    null,
                    contactoPrincipal,
                    contactosSecundarios
            );
        }

        if(tipoPersona.equals("JURIDICA")) {
            List<MedioContacto> contactosSecundarios = contactoSecundario == null ? null : List.of(contactoSecundario);

            return new PersonaJuridica(
                    nombreCompleto,
                    TipoOrganizacion.SIN_ESPECIFICAR,
                    null,
                    documento,
                    null,
                    contactoPrincipal,
                    contactosSecundarios
            );
        }

        throw new IllegalArgumentException("Tipo de persona invalido debe revisar: " + tipoPersona);
    }
}




