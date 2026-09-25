package ar.edu.utn.frba.dds.donatrack.logistica.broker.dto;

public record GpsMensaje (
  String imei,
  String nivelBateria,
  String latitud,
  String longitud
) { }