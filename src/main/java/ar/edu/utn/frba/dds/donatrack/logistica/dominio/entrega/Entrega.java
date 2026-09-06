package ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "entregas")
public class Entrega {
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  @ElementCollection
  @CollectionTable(name = "entrega_donaciones", joinColumns = @JoinColumn(name = "entrega_id"))
  private List<DonacionEnTransito> donaciones;

  @ManyToOne
  @JoinColumn(name = "camion_patente")
  private Camion camionAsignado;

  @ElementCollection
  @CollectionTable(name = "entrega_historial_estados", joinColumns = @JoinColumn(name = "entrega_id"))
  @OrderColumn(name = "orden")
  private List<EstadoEntrega> historialEstados;

  @ElementCollection
  @CollectionTable(name = "entrega_fotos_recepcion", joinColumns = @JoinColumn(name = "entrega_id"))
  @Column(name = "url_foto")
  private List<String> fotosRecepcion;

  public Entrega(List<DonacionEnTransito> donaciones, Camion camion) {
    this.donaciones = donaciones;
    this.camionAsignado = camion;
    this.historialEstados = new ArrayList<>();
    this.fotosRecepcion = new ArrayList<>();

    historialEstados.add(new EstadoEntrega(TipoEstadoEntrega.PENDIENTE, camion));
  }

  public TipoEstadoEntrega getEstadoActual(){
    return historialEstados
        .get(historialEstados.size()-1)
        .getTipoEstado();
  }

  public void reasignarCamion(Camion camion) {
    validarTransicionDesde(TipoEstadoEntrega.PENDIENTE, "reasignar camión");
    this.camionAsignado = camion;
  }

  public void agregarFotoRecepcion(String url) {
    if (getEstadoActual() != TipoEstadoEntrega.ENTREGADA)
      throw new IllegalStateException("Solo se pueden cargar fotos de una entrega ya confirmada como entregada");

    if (url == null || url.isBlank())
      throw new DominioException("La URL de la foto es obligatoria");

    fotosRecepcion.add(url);
  }

  public void confirmarListaParaEntregar() {
    validarTransicionDesde(TipoEstadoEntrega.PENDIENTE, "confirmar como lista para entregar");
    cambiarEstado(TipoEstadoEntrega.LISTA_PARA_ENTREGAR, "Asignada a camión " + camionAsignado.getPatente());
  }

  public void iniciarTraslado() {
    validarTransicionDesde(TipoEstadoEntrega.LISTA_PARA_ENTREGAR, "iniciar traslado");
    cambiarEstado(TipoEstadoEntrega.EN_TRASLADO, "Iniciando recorrido");
  }

  public void confirmarRecepcion() {
    validarTransicionDesde(TipoEstadoEntrega.EN_TRASLADO, "confirmar recepción");
    cambiarEstado(TipoEstadoEntrega.ENTREGADA, null);
  }

  public void marcarNoRecibida(String motivo) {
    if (motivo == null || motivo.isBlank())
      throw new DominioException("Debe indicar un motivo");

    validarTransicionDesde(TipoEstadoEntrega.EN_TRASLADO, "marcar como no recibida");
    cambiarEstado(TipoEstadoEntrega.NO_RECIBIDA, motivo);
  }

  public void reingresarDeposito() {
    validarTransicionDesde(TipoEstadoEntrega.NO_RECIBIDA, "reingresar a depósito");
    cambiarEstado(TipoEstadoEntrega.PENDIENTE, "Entrega devuelta al depósito");
  }

  //==================== FUNCIONES AUXILIARES =====================
  private void cambiarEstado(TipoEstadoEntrega estado, String detalle) {
    historialEstados.add(new EstadoEntrega(estado, detalle, camionAsignado));
  }

  private void validarTransicionDesde(TipoEstadoEntrega esperado, String accion) {
    if (getEstadoActual() != esperado)
      throw new CambioDeEstadoNoPermitidoException("No se puede " + accion + " desde el estado " + getEstadoActual());
  }

  public Beneficiario getDestino() {
    return donaciones.get(0).getBeneficiario();
  }

}