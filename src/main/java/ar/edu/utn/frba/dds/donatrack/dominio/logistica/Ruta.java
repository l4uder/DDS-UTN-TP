package ar.edu.utn.frba.dds.donatrack.dominio.logistica;

import java.time.LocalDate;
import java.util.List;

public class Ruta {
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
}