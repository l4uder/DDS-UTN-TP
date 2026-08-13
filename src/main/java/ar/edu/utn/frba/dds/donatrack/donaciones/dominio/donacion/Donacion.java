package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Setter;

@Getter
public class Donacion {
  @Setter
  private String id;
  private String descripcion;
  private List<Bien> bienes;
  private List<EstadoDonacion> historialEstados;
  private Beneficiario beneficiario;//Doble asociación bidireccional
  private List<Donante> donantes;//Todo después ver: el bien debería tener al donante
  private Boolean estadoModificable;

  public Donacion(List<Bien> bienes, List<Donante> donantes) {
    checkDatos(bienes, donantes);
    this.descripcion = this.descripcionGeneral(bienes);
    this.bienes = new ArrayList<>(bienes);
    this.historialEstados = new ArrayList<>();
    this.historialEstados.add(new EstadoDonacion(TipoEstadoDonacion.EN_DEPOSITO));
    this.donantes = donantes;
    this.estadoModificable = true;
  }

  private void checkDatos(List<Bien> bienes, List<Donante> donantes) {
    if (bienes == null || bienes.isEmpty()) {
      throw new DominioException("Una donación debe tener al menos un bien");
    }
    if (donantes == null || donantes.isEmpty()) {
      throw new DominioException("Una donación debe tener al menos un donante");
    }
  }

  public TipoEstadoDonacion getEstadoActual() {
    return historialEstados.get(historialEstados.size() - 1).getTipoEstado();
  }

  public LocalDateTime getFechaAsignacion() {
    return this.historialEstados.stream()
        .filter(e -> e.getTipoEstado() == TipoEstadoDonacion.ASIGNACION_REALIZADA)
        .findFirst()
        .map(EstadoDonacion::getFecha)
        .orElseThrow(() -> new DominioException("Donación no posee fecha de asignación"));
  }

  /*Solo para poder probar un test despues ver como mejorar quitando esto*/
  public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
    EstadoDonacion estadoAsignacion = this.historialEstados.stream()
        .filter(e -> e.getTipoEstado() == TipoEstadoDonacion.ASIGNACION_REALIZADA)
        .findFirst()
        .orElseThrow(() -> new DominioException("Donación no posee fecha de asignación"));
    estadoAsignacion.setFecha(fechaAsignacion);
  }

  public String descripcionGeneral(List<Bien> bienes) {
    return bienes.stream()
        .map(b -> b.getCantidad() + " " + b.getUnidadMedida() + " de " + b.getDescripcion())
        .collect(Collectors.joining(", "));
  }

  // [En deposito] -> [Asignacion Realizada]
  public void confirmarAsignacion(Beneficiario beneficiario) {
    if (getEstadoActual() != TipoEstadoDonacion.EN_DEPOSITO)
      throw new CambioDeEstadoNoPermitidoException("No se puede asignar donacion a menos que este en deposito");

    setEstadoActual(TipoEstadoDonacion.ASIGNACION_REALIZADA, "Se realizó la asignación a " + beneficiario.getRazonSocial());

    this.beneficiario = beneficiario; //Doble asignación
    this.beneficiario.asignarDonacion(this); //Doble asignación

    this.beneficiario.recibirNotificacion("Se le ha asignado una nueva donación: " + this.descripcion);
    this.donantes.forEach(d -> d.recibirNotificacion("Tu donación ha sido asignada a: " + this.beneficiario.getRazonSocial()));
  }
  // [Asignación Realizada] -> [Lista Para Entregar]
  public void confirmarListaParaEntregar() {
    if (getEstadoActual() != TipoEstadoDonacion.ASIGNACION_REALIZADA)
      throw new CambioDeEstadoNoPermitidoException("La donacion debe estar asignada para confirmar ruta");

    setEstadoActual(TipoEstadoDonacion.LISTA_PARA_ENTREGAR, null);
  }
  // [Lista Para Entregar] -> [En Traslado]
  public void confirmarEnTraslado() {
    if (getEstadoActual() != TipoEstadoDonacion.LISTA_PARA_ENTREGAR)
      throw new CambioDeEstadoNoPermitidoException("La donación debe estar en lista para entregar, para iniciar el traslado");

    setEstadoActual(TipoEstadoDonacion.EN_TRASLADO, null);

    String mensaje = "La entrega está en camino. ¡Atentos al recorrido!";
    this.beneficiario.recibirNotificacion(mensaje);
    this.donantes.forEach(d -> d.recibirNotificacion(mensaje));
    //La entrega del enlace al mapa y el chofer se lo dejamos al Notificador
  }
  // [En Traslado] -> [Entregada] FIN
  public void confirmarEntrega() {
    if (getEstadoActual() != TipoEstadoDonacion.EN_TRASLADO)
      throw new CambioDeEstadoNoPermitidoException("La donación debe estar en traslado para confirmar entrega");

    setEstadoActual(TipoEstadoDonacion.ENTREGADA, null);
    this.estadoModificable = false;

    String mensaje = "¡Entrega finalizada con éxito!";
    this.beneficiario.recibirNotificacion(mensaje);
    this.donantes.forEach(d -> d.recibirNotificacion(mensaje));
    //La entregar del comprobante se lo dejamos al Notificador
  }
  // [En Traslado] -> [Entregada Fallida]
  public void errorAlEntregar(String observacion) {
    if (getEstadoActual() != TipoEstadoDonacion.EN_TRASLADO)
      throw new CambioDeEstadoNoPermitidoException("La donación debe estar en traslado para poder notificar entrega fallida");

    if (observacion == null || observacion.isBlank())
      throw new DominioException("Es necesario una observación en la entrega fallida");

    setEstadoActual(TipoEstadoDonacion.ENTREGA_FALLIDA, observacion);

    String mensaje = "La entrega no pudo concretarse. Motivo: " + observacion;
    this.beneficiario.recibirNotificacion(mensaje);
    this.donantes.forEach(d -> d.recibirNotificacion(mensaje));
    //La notificación a los admins se lo dejamos al Notificador
  }
  // [Entrega Fallida] -> [En Deposito]
  public void RetornarADeposito() {
    if (getEstadoActual() != TipoEstadoDonacion.ENTREGA_FALLIDA)
      throw new CambioDeEstadoNoPermitidoException("No se puede recibir en deposito a menos que la entrega falle");

    setEstadoActual(TipoEstadoDonacion.EN_DEPOSITO, null);
  }
  // [En Deposito] -> [Vencida] FIN
  public void marcarVencida() {
    if (getEstadoActual() != TipoEstadoDonacion.EN_DEPOSITO)
      throw new CambioDeEstadoNoPermitidoException("No se puede marcar como vencida si no esta en deposito");

    setEstadoActual(TipoEstadoDonacion.VENCIDA, null);
    this.estadoModificable = false;

    this.donantes.forEach(d -> d.recibirNotificacion("La donación se venció, lo sentimos mucho"));
  }

  public Subcategoria getSubcategoria() {
    return this.bienes.get(0).getSubcategoria(); // todos tienen la misma
  }

  public void actualizarBienes(List<Bien> nuevosBienes) {
    if (nuevosBienes == null || nuevosBienes.isEmpty())
      throw new DominioException("Una donación debe tener al menos un bien");

    if (getEstadoActual() != TipoEstadoDonacion.EN_DEPOSITO)
      throw new CambioDeEstadoNoPermitidoException("Solo se puede modificar una donación que esta en deposito");

    this.bienes = new ArrayList<>(nuevosBienes);
    this.descripcion = this.descripcionGeneral(nuevosBienes);
  }

  //====================== FUNCIONES AUXILIARES ==========================
  private void setEstadoActual(TipoEstadoDonacion estado, String observacion) {
    if (!this.estadoModificable)
      throw new CambioDeEstadoNoPermitidoException("No se puede modificar su estado por que esta en: " + this.getEstadoActual());

    this.historialEstados.add(new EstadoDonacion(estado, observacion));
  }

}