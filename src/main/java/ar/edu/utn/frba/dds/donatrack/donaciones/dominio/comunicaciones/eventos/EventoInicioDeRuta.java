package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicacionescambioestado.eventos;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;

public record EventoInicioDeRuta(
    Beneficiario beneficiario,
    String detalleDonacion,
    String linkMapa
) { }
