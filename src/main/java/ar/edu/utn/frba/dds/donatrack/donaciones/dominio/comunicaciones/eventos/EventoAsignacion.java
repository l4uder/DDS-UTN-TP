package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.eventos;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import java.util.List;

public record EventoAsignacionDeDonacion(
    Beneficiario beneficiario,
    List<Donante> donantes,
    String detalleDonacion
) { }
