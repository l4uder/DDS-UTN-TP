package ar.edu.utn.frba.dds.donatrack.logistica.dominio;

import java.time.LocalDate;
import java.util.List;

public class Ruta {
  private String id;
  private Camion camion;
  private Chofer chofer;
  private LocalDate fecha;
  private List<Entrega> entregasOrdenadas;
  private boolean iniciada;

  public Ruta(Camion camion, LocalDate fecha, List<Entrega> entregasOrdenadas) {
    this.camion = camion;
    this.chofer = null;
    this.fecha = fecha;
    this.entregasOrdenadas = entregasOrdenadas;
    this.iniciada = false;
  }

  public String getId() { return id; }

  public void iniciarRecorrido() {
    if (iniciada) {
      throw new IllegalStateException("La ruta ya fue iniciada");
    }
    if (chofer == null) {
      throw new IllegalStateException("La ruta debe tener chofer");
    }
    this.iniciada = true;
    this.entregasOrdenadas.forEach(e -> e.iniciarTraslado());
  }

  public void asignarChofer(Chofer chofer) {
    if (this.chofer != null) {
      throw new IllegalStateException("La ruta ya tiene un chofer asignado");
    }
    this.chofer = chofer;
  }

  public LocalDate getFecha() { return fecha; }
  public boolean isIniciada() {
    return iniciada;
  }

  public Camion getCamion() {
    return camion;
  }

  public Chofer getChofer() {
    return chofer;
  }

  public List<Entrega> getEntregasOrdenadas() {
    return entregasOrdenadas;
  }

  public void setId(String id) {
    this.id = id;
  }
}