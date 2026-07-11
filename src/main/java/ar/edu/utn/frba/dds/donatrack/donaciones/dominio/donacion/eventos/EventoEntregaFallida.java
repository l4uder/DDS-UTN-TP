package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;

import java.time.LocalDate;

public record EventoEntregaFallida(
        String observacion,
        Donacion donacion,
        LocalDate date
) {
}
