package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicacionescambioestado.eventos;

public record EventoEntregaFallida(
    String donacionId,
    String observacion
) { }
