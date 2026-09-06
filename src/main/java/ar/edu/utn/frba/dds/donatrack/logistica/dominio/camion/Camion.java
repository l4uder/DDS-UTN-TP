package ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Camiones")
public class Camion {
  @Id
  private String patente;
  @Column(name = "capacidad_volumen")
  private float capacidadVolumen;
  @Column(name = "altura")
  private float altura;
  @Column(name = "capacidad_carga")
  private float capacidadCarga;
  @ElementCollection
  @CollectionTable(name = "camion_coordenadas", joinColumns = @JoinColumn(name = "camion_patente"))
  private List<Coordenada> coordenadas;
  @OneToOne
  @JoinColumn(name = "gps_imei")
  private Gps gps;

  public Camion(String patente, Float capacidadVolumen, Float altura, Float capacidadCarga) {
    validar(patente, capacidadVolumen, altura, capacidadCarga);
    this.patente = patente.trim();
    this.capacidadVolumen = capacidadVolumen;
    this.altura = altura;
    this.capacidadCarga = capacidadCarga;
    this.coordenadas = new ArrayList<>();
    this.gps = null;
  }

  private void validar(String patente, Float capacidadVolumen, Float altura, Float capacidadCarga) {
    validarPatente(patente);
    validarCapacidades(capacidadVolumen, altura, capacidadCarga);
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
    return this.coordenadas.isEmpty() ? null : this.coordenadas.get(this.coordenadas.size() - 1);
  }

  public String getLinkSeguimiento() {
    Coordenada ubicacion = getUbicacionActual();
    if (ubicacion == null) {
      return "https://404/Sin-direccion";
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

  //======================= FUNCIONES AUXILIARES =======================
  private void validarPatente(String patente) {
    if (patente == null || patente.isBlank()) {
      throw new DominioException("El campo 'patente' es obligatorio");
    }
  }

  private void validarCapacidades(Float capacidadVolumen, Float altura, Float capacidadCarga) {
    validarPositivo(capacidadVolumen, "capacidad_volumen");
    validarPositivo(altura, "altura");
    validarPositivo(capacidadCarga, "capacidad_carga");
  }

  private void validarPositivo(Float valor, String campo) {
    if (valor == null) {
      throw new DominioException("El campo '" + campo + "' es obligatorio");
    }
    if (valor <= 0) {
      throw new DominioException("El campo '" + campo + "' debe ser mayor a 0");
    }
  }

}
