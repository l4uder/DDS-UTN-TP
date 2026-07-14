package ar.edu.utn.frba.dds.donatrack.donaciones.service;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.EstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoEntregaExitosa;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoEntregaFallida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoInicioDeRuta;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.server.AppEventBus;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;

import java.time.LocalDate;
import java.util.List;

public class DonacionService {

  private final DonacionRepository repository = DonacionRepository.getInstancia();

  public List<Donacion> listar(String estado) {
    if (estado == null) {
      return repository.buscarTodos();
    }
    return repository.buscarPorEstado(parseEstado(estado));
  }

  public Donacion obtener(String id) {
    Donacion donacion = this.repository.buscarPorId(id);
    if (donacion == null) throw new RecursoNoEncontradoException("No existe donación: " + id);

    return donacion;
  }

  public Donacion crear(List<Bien> bienes, List<String> donanteIds) {
    List<Donante> donantes = donanteIds.stream()
      .map(id -> DonanteRepository.getInstancia().buscarPorId(id)
      .orElseThrow(() -> new DomainValidationException("Donante no encontrado con ID: " + id)))
      .toList();

    Donacion donacion = new Donacion(bienes, donantes);
    repository.guardarDonacion(donacion);
    return donacion;
  }

  public Donacion actualizar(String id, List<Bien> bienes) {
    Donacion donacion = obtener(id);
    donacion.reemplazarBienes(bienes);
    repository.guardarDonacion(donacion);
    return donacion;
  }

  public void eliminar(String id) {
    obtener(id);
    repository.eliminar(id);
  }

  public Donacion cambiarEstadoVencida(String id) {
    Donacion donacion = obtener(id);
    donacion.marcarVencida();
    repository.guardarDonacion(donacion);
    return donacion;
  }

  public Donacion cambiarEstadoEnDeposito(String id) {
    Donacion donacion = obtener(id);
    donacion.confirmarRecepcionDeposito();
    repository.guardarDonacion(donacion);
    return donacion;
  }

  public Donacion cambiarEstadoListaEntregar(String id) {
    Donacion donacion = obtener(id);
    donacion.confirmarRuta();
    repository.guardarDonacion(donacion);
    return donacion;
  }

  public Donacion cambiarEstadoEntregada(String id, String idCamion) {
    Donacion donacion = obtener(id);
    donacion.confirmarEntrega();
    AppEventBus.getInstance().post(new EventoEntregaExitosa(donacion, LocalDate.now(), idCamion));
    repository.guardarDonacion(donacion);
    return donacion;
  }

  public Donacion cambiarEstadoErrorEntrega(String id, String observacion) {
    Donacion donacion = obtener(id);
    donacion.notificarEntregaFallida(observacion);
    AppEventBus.getInstance().post(new EventoEntregaFallida(observacion, donacion, LocalDate.now()));
    repository.guardarDonacion(donacion);
    return donacion;
  }

  public Donacion cambiarEstadoEnTraslado(String id, String linkMapa) {
    Donacion donacion = obtener(id);
    donacion.confirmarTrasladoEnCurso();
    AppEventBus.getInstance().post(new EventoInicioDeRuta(donacion, LocalDate.now(), linkMapa));
    repository.guardarDonacion(donacion);
    return donacion;
  }

  public List<EstadoDonacion> listarEstados(String id) {
    return obtener(id).getHistorialEstados();
  }

  private TipoEstadoDonacion parseEstado(String estado) {
    try {
      return TipoEstadoDonacion.valueOf(estado.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DomainValidationException("Estado invalido: " + estado);
    }
  }

}
