package ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.ArrayList;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "rutas")
public class Ruta {
  @Id
  @Setter
  private String id;

  @ManyToOne
  @JoinColumn(name = "camion_patente")
  private Camion camion;

  @ManyToOne
  @JoinColumn(name = "chofer_licencia")
  private Chofer chofer;

  @Column(name = "fecha")
  private LocalDate fecha;

  @OneToMany
  @JoinColumn(name = "ruta_id")
  @OrderColumn(name = "orden_entrega")
  private List<Entrega> entregasOrdenadas;

  @Column(name = "esta_iniciada")
  private boolean estaIniciada;

  public Ruta(Camion camion, LocalDate fecha, List<Entrega> entregasOrdenadas) {
    validar(camion, fecha, entregasOrdenadas);
    this.id = null;
    this.camion = camion;
    this.fecha = fecha;
    this.entregasOrdenadas = new ArrayList<>(entregasOrdenadas);
    this.estaIniciada = false;
  }

  private void validar(Camion camion, LocalDate fecha, List<Entrega> entregasOrdenadas) {
    if (camion == null)
      throw new DominioException("La ruta debe tener un camión asignado");

    if (fecha == null)
      throw new DominioException("La ruta debe tener fecha");

    if (entregasOrdenadas == null)
      throw new DominioException("La ruta debe tener entregas");
  }

  public void asignarChofer(Chofer chofer) {
    if (this.chofer != null) {
      throw new DominioException("La ruta ya tiene chofer asignado");
    }

    this.chofer = chofer;
  }

  public void iniciarRecorrido() {
    if (estaIniciada)
      throw new DominioException("La ruta ya fue iniciada");

    if (chofer == null)
      throw new DominioException("La ruta debe tener chofer");

    estaIniciada = true;
    entregasOrdenadas.forEach(Entrega::iniciarTraslado);
  }

}