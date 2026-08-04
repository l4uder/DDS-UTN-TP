package ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.ValidacionDominioException;
import java.util.ArrayList;

import java.time.LocalDate;
import java.util.List;
import lombok.Setter;

public class Ruta {
  @Setter
  private String id;
  private Camion camion;
  private Chofer chofer;
  private LocalDate fecha;
  private List<Entrega> entregasOrdenadas;
  private boolean iniciada;


  public Ruta(
      Camion camion,
      LocalDate fecha,
      List<Entrega> entregasOrdenadas
  ) {

    if (camion == null) {
      throw new ValidacionDominioException(
          "La ruta debe tener un camión asignado"
      );
    }

    if (fecha == null) {
      throw new ValidacionDominioException(
          "La ruta debe tener fecha"
      );
    }

    if (entregasOrdenadas == null) {
      throw new ValidacionDominioException(
          "La ruta debe tener entregas"
      );
    }

    this.id = null; //dejamos que el repo le asigne su id
    this.camion = camion;
    this.fecha = fecha;
    this.entregasOrdenadas = new ArrayList<>(entregasOrdenadas);
    this.iniciada = false;
  }


  public void iniciarRecorrido() {

    if (iniciada) {
      throw new IllegalStateException(
          "La ruta ya fue iniciada"
      );
    }

    if (chofer == null) {
      throw new IllegalStateException(
          "La ruta debe tener chofer"
      );
    }

    iniciada = true;

    entregasOrdenadas.forEach(
        Entrega::iniciarTraslado
    );
  }


  public void asignarChofer(Chofer chofer) {

    if (chofer == null) {
      throw new ValidacionDominioException(
          "El chofer no puede ser nulo"
      );
    }

    if (this.chofer != null) {
      throw new IllegalStateException(
          "La ruta ya tiene chofer asignado"
      );
    }

    this.chofer = chofer;
  }


  public String getId() {
    return id;
  }

  public Camion getCamion() {
    return camion;
  }

  public Chofer getChofer() {
    return chofer;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public List<Entrega> getEntregasOrdenadas() {
    return List.copyOf(entregasOrdenadas);
  }

  public boolean isIniciada() {
    return iniciada;
  }
}