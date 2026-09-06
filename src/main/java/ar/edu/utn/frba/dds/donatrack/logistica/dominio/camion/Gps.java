package ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "gps")
public class Gps {
  @Id
  private String imei;
  @Column(name = "fecha")
  private LocalDateTime fecha;
  @Column(name = "nivel_bateria")
  private String nivelBateria;
  @Column(name = "sigue_funcionando")
  private Boolean funciona;

  public Gps(String id) {
    this.imei = id;
    this.nivelBateria = "100";
    this.funciona = true;
  }

  public void desactivar() {
    this.funciona = false;
  }

  public void actualizarEstado(String nivelBateria) {
    this.fecha = LocalDateTime.now();
    this.nivelBateria = nivelBateria;
  }
}