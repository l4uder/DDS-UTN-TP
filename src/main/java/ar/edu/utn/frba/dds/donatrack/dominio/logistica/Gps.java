package ar.edu.utn.frba.dds.donatrack.dominio.logistica;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Gps {
  private String imei;
  private LocalDateTime fecha;
  private String nivelBateria;
  private Boolean funciona;

  public Gps(String id) {
    this.imei = id;
    this.nivelBateria = "100";
    this.funciona = true;
  }

  public void dejoFuncionar() {
    this.funciona = false;
  }

  public void actualizarEstado(String nivelBateria) {
    this.fecha = LocalDateTime.now();
    this.nivelBateria = nivelBateria;
  }
}
