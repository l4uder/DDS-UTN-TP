package ar.edu.utn.frba.dds.donatrack.logistica.dominio.planificacion;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Lote {
  private static final int MAX_DONACIONES_POR_LOTE = 100;
  private final List<Entrega> entregas;

  private Lote(List<Entrega> entregas) {
    this.entregas = new ArrayList<>(entregas);
  }

  public boolean estaVacio() { return entregas.isEmpty(); }

  /** Cantidad total de donaciones que arrastran las entregas de este lote. */
  public int cantidadDonaciones() {
    return entregas.stream().mapToInt(e -> e.getDonaciones().size()).sum();
  }

  /**
   * Arma la lista de lotes a partir de las entregas, respetando el límite de
   * MAX_DONACIONES_POR_LOTE donaciones por lote. Una Entrega cuya cantidad de
   * donaciones ya supere el límite por sí sola igual entra en su propio lote,
   * ya que no se puede partir una Entrega entre lotes distintos.
   */
  public static List<Lote> armarLotes(List<Entrega> entregas) {
    List<Lote> lotes = new ArrayList<>();
    Lote actual = new Lote(new ArrayList<>());

    for (Entrega entrega : entregas) {
      if (!actual.puedeAgregar(entrega) && !actual.estaVacio()) {
        lotes.add(actual);
        actual = new Lote(new ArrayList<>());
      }
      actual = actual.agregando(entrega);
    }
    if (!actual.estaVacio()) {
      lotes.add(actual);
    }
    return lotes;
  }

  //================== FUNCIONES AUXILIARES ===================
  /** Indica si agregar esta entrega mantendría el lote dentro del límite. */
  private boolean puedeAgregar(Entrega entrega) {
    return estaVacio()
        || cantidadDonaciones() + entrega.getDonaciones().size() <= MAX_DONACIONES_POR_LOTE;
  }

  private Lote agregando(Entrega entrega) {
    List<Entrega> nuevas = new ArrayList<>(entregas);
    nuevas.add(entrega);
    return new Lote(nuevas);
  }

}