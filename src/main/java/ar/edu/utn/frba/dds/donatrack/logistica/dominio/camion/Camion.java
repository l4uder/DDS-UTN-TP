package ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Camion {
  private final String patente;
  private float capacidadVolumen;
  private float altura;
  private float capacidadCarga;
  private List<Coordenada> coordenadas;
  private Gps gps;

  public Camion(String patente, Float capacidadVolumen,
                Float altura, Float capacidadCarga) {
    validarPatente(patente);
    validarCapacidades(capacidadVolumen, altura, capacidadCarga);

    this.patente = patente.trim();
    this.capacidadVolumen = capacidadVolumen;
    this.altura = altura;
    this.capacidadCarga = capacidadCarga;
    this.coordenadas = new ArrayList<>();
    this.gps = null;
  }

  private void validarPatente(String patente) {
    if (patente == null || patente.isBlank()) {
      throw new DominioException("El campo 'patente' es obligatorio");
    }
  }

  private void validarCapacidades(Float capacidadVolumen, Float altura, Float capacidadCarga) {
    validarPositivo(capacidadVolumen, "capacidadVolumen");
    validarPositivo(altura, "altura");
    validarPositivo(capacidadCarga, "capacidadCarga");
  }

  private void validarPositivo(Float valor, String campo) {
    if (valor == null) {
      throw new DominioException("El campo '" + campo + "' es obligatorio");
    }
    if (valor <= 0) {
      throw new DominioException(
          "El campo '" + campo + "' debe ser mayor a 0");
    }
  }

  public void agregarGps(Gps gps) {
    this.gps = gps;
  }

  public boolean posee(String idGps) {
    return this.gps != null && this.gps.getImei().equalsIgnoreCase(idGps);
  }

  public void agregarCoordenada(Coordenada coordenada) {
    this.coordenadas.add(coordenada);
  }

  public Coordenada getUbicacionActual() {
    if (this.coordenadas.isEmpty()) {
      return null;
    }
    return this.coordenadas.get(this.coordenadas.size() - 1);
  }

  public String getLinkSeguimiento() {
    Coordenada ubicacion = getUbicacionActual();
    if (ubicacion == null) {
      return null;
    }
    return "https://maps.google.com/?q=" + ubicacion.getLatitud() + "," + ubicacion.getLongitud();
  }

  public void actualizarDatos(Float capacidadVolumen, Float altura, Float capacidadCarga, Gps gps) {
    validarCapacidades(capacidadVolumen, altura, capacidadCarga);

    this.capacidadVolumen = capacidadVolumen;
    this.altura = altura;
    this.capacidadCarga = capacidadCarga;
    this.gps = gps;
  }

}
