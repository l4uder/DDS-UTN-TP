package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.eventos;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;

public record EventoEntregaFallida(
    Donacion donacion,
    String observacion
) { }
